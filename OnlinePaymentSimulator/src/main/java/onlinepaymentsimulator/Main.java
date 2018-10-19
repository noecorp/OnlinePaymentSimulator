package onlinepaymentsimulator;

import java.sql.Connection;
import java.sql.DriverManager;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import onlinepaymentsimulator.dao.AccountDao;
import onlinepaymentsimulator.dao.PINCodeDao;
import onlinepaymentsimulator.dao.PaymentDao;
import onlinepaymentsimulator.dao.UserDao;
import onlinepaymentsimulator.domain.Account;
import onlinepaymentsimulator.domain.Balance;
import onlinepaymentsimulator.domain.Customer;
import onlinepaymentsimulator.domain.PINCode;
import onlinepaymentsimulator.domain.Payment;
import onlinepaymentsimulator.domain.Token;
import onlinepaymentsimulator.domain.User;
import org.mindrot.jbcrypt.BCrypt;
import spark.ModelAndView;
import spark.Request;
import spark.Spark;
import spark.template.thymeleaf.ThymeleafTemplateEngine;

public class Main {

    public static String BANK_NAME = "Demo Pankki";
    private static String sessionCookieKey = "s";
    private static String PRE_SELECTED_ACCOUNT_ID_KEY = "preSelAcc";
    private static String PAYMENT_INFO = "paymentInfo";
    private static String[] PAYMENT_INFO_KEYS = new String[]{"to", "toIBAN", "amount", "currency", "ref"};

    public static void main(String[] args) throws Exception {
        // A little simple cache for a token-user mapping and token-account
        Map<Token, User> userTokens = new HashMap<>();
        Map<String, Account> accountCache = new HashMap<>();
        Map<String, Map<String, Object>> tempSubmitCache = new HashMap<>();

        // Generate hash: String hashed = BCrypt.hashpw("1234", BCrypt.gensalt());
        // Get {url}/unauthorized
        Spark.get("/unauthorized", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("BANK_NAME", Main.BANK_NAME);

            return new ModelAndView(map, "unauthorized");
        }, new ThymeleafTemplateEngine());

        // Get {url}/demo-payments
        Spark.get("/demo-payments", (req, res) -> {
            HashMap map = new HashMap<>();
            return new ModelAndView(map, "demo-payments");
        }, new ThymeleafTemplateEngine());

        // Get {url}/
        Spark.get("/", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("BANK_NAME", Main.BANK_NAME);

            Set<String> params = req.queryParams();
            List<String> paramValues = new ArrayList<>();

            List<String> keys = Arrays.asList(PAYMENT_INFO_KEYS);

            for (String param : keys) {
                paramValues.add(req.queryParams(param));
            }

            String paymentInfo = String.join("|", paramValues);
            res.cookie(PAYMENT_INFO, paymentInfo.replace(" ", "%20"));

            return new ModelAndView(map, "index");
        }, new ThymeleafTemplateEngine());

        // Get {url}/auth
        Spark.get("/auth", (req, res) -> {

            HashMap map = new HashMap<>();
            Connection conn = getConnection();

            // If the user is not null...
            User user = Main.findUserByToken(userTokens, req);
            if (user != null) {
                PINCode pin = new PINCodeDao(conn).getRandomPIN(user);

                map.put("BANK_NAME", Main.BANK_NAME);
                map.put("accounts", new AccountDao(conn).findAccountsByUser(user.getId()));
                map.put("bank_pin", pin.getBankCode());
                map.put("acc", req.queryParams(Main.PRE_SELECTED_ACCOUNT_ID_KEY));
            } else {
                conn.close();
                res.redirect("/");
            }

            conn.close();
            return new ModelAndView(map, "auth");
        }, new ThymeleafTemplateEngine());

        // Get {url}/confirm
        Spark.get("/confirm", (req, res) -> {

            HashMap map = new HashMap<>();
            Connection conn = getConnection();

            // If the user is not null...
            Token token = Main.findToken(userTokens.keySet(), req.cookie(Main.sessionCookieKey));
            User user = Main.findUserByToken(userTokens, req);

            if (user == null || token == null) {
                res.redirect("/unauthorized");
            }

            map.put("BANK_NAME", Main.BANK_NAME);

            Account acc = accountCache.get(req.cookie(sessionCookieKey));
            map.put("account", new AccountDao(conn).findByIdWithNoUser(acc.getId()));

            if (accountCache.keySet().contains(token)) {
                accountCache.remove(req.cookie(sessionCookieKey));
            }

            map = (HashMap) getFormValues(map, req);
            
            if (!map.containsKey("ref")) {
                map.put("ref", "(ei viitettä)");
            }

            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

            Customer cust = user.getCustomer();

            map.put("expireDate", sdf.format(new Date()));
            map.put("customer", (cust != null) ? cust.getName() : "(ei nimeä)");

            tempSubmitCache.put(req.cookie(sessionCookieKey), map);

            conn.close();
            return new ModelAndView(map, "confirm");
        }, new ThymeleafTemplateEngine());

        // Get {url}/submitted
        Spark.get("/submitted", (req, res) -> {

            HashMap map = new HashMap<>();
            Connection conn = getConnection();

            // If the user is not null...
            Token token = Main.findToken(userTokens.keySet(), req.cookie(Main.sessionCookieKey));
            User user = Main.findUserByToken(userTokens, req);

            tempSubmitCache.remove(req.cookie(sessionCookieKey));

            if (user == null || token == null) {
                res.redirect("/unauthorized");
            }

            map.put("BANK_NAME", Main.BANK_NAME);

            conn.close();
            return new ModelAndView(map, "submitted");
        }, new ThymeleafTemplateEngine());

        // Post {url}/submitted
        Spark.post("/submitted", (req, res) -> {

            HashMap map = new HashMap<>();
            Connection conn = getConnection();

            // Check operation...
            Token token = Main.findToken(userTokens.keySet(), req.cookie(Main.sessionCookieKey));
            User user = Main.findUserByToken(userTokens, req);

            if (user == null || token == null || !tempSubmitCache.containsKey(token.getToken())) {
                res.redirect("/unauthorized");
            }
            
            map = (HashMap) getFormValues(map, req);

            Account acc = accountCache.get(req.cookie(sessionCookieKey));
            
            Payment payment = new Payment(Balance.fromString(String.valueOf(map.get("amount"))), new Date(), String.valueOf(req.queryParams("msgToReceiver")), acc, String.valueOf(map.get("toIBAN")), (Integer) Integer.parseInt(String.valueOf(map.get("ref"))));
            
            // Save payment
            PaymentDao dao = new PaymentDao(conn);
            dao.savePaymentToDatabase(payment);
            
            res.redirect("/submitted");

            conn.close();
            return "";
        });

        // Post {url}/login
        Spark.post("/login", (req, res) -> {
            Connection conn = getConnection();
            
            Integer username = Integer.parseInt(req.queryParams("user"));

            UserDao userDao = new UserDao(conn);
            Token token = userDao.login(username, req.queryParams("password"));
            
            if (token == null) {
                conn.close();
                res.redirect("/");
                return "";
            }

            User user = userDao.single(username);

            if (user == null) {
                conn.close();
                res.redirect("/");
                return "";
            }

            userTokens.put(token, user);

            long timeLeft = token.timeLeft(new Date());
            res.cookie("s", token.getToken(), (int) timeLeft);
            conn.close();
            res.redirect("/auth");
            return "";
        });

        // Post {url}/confirmed
        Spark.post("/confirmed", (req, res) -> {
            // If the user is not null...
            User user = Main.findUserByToken(userTokens, req);
            Connection conn = getConnection();

            if (user != null) {
                Integer bankPIN = Integer.parseInt(req.queryParams("bankPIN"));
                Integer accountId = Integer.parseInt(req.queryParams("selectedAccount"));
                Integer inputtedPIN = Integer.parseInt(req.queryParams("code"));

                boolean pinOK = new PINCodeDao(conn).checkPIN(bankPIN, inputtedPIN, user);

                if (!pinOK) {
                    res.redirect("/auth?" + PRE_SELECTED_ACCOUNT_ID_KEY + "=" + accountId);
                }

                accountCache.put(req.cookie(sessionCookieKey), new AccountDao(conn).findByIdWithNoUser(accountId));

                res.redirect("/confirm");
            } else {
                res.redirect("/unauthorized");
            }

            conn.close();
            return "";
        });

        // Post {url}/summary
        Spark.post("/summary", (req, res) -> {
            // If the user is not null...
            User user = Main.findUserByToken(userTokens, req);

            if (user != null) {
                res.redirect("/confirm");
            }

            res.redirect("/unauthorized");

            return "";
        });
    }

    public static User findUserByToken(Map<Token, User> tokens, Request request) {
        Token token = findToken(tokens.keySet(), request.cookie(Main.sessionCookieKey));
        return Main.findUserByToken(tokens, token);
    }

    public static User findUserByToken(Map<Token, User> tokens, String cookie) {
        Token token = findToken(tokens.keySet(), cookie);
        return Main.findUserByToken(tokens, token);
    }

    public static User findUserByToken(Map<Token, User> tokens, Token token) {
        return tokens.get(token);
    }

    public static Token findToken(Set<Token> tokens, String wantedToken) {
        for (Token token : tokens) {
            if (token.getToken().equals(wantedToken) && token.getExpires().after(new Date())) {
                return token;
            }
        }

        return null;
    }

    public static Map getFormValues(Map map, Request req) {
        String paymentInfo = req.cookie(PAYMENT_INFO).replace("%20", " ");
        String[] splittedInfo = paymentInfo.split("\\|");

        List<String> keys = Arrays.asList(PAYMENT_INFO_KEYS);
        for (String key : keys) {
            map.put(key, splittedInfo[keys.indexOf(key)]);
        }

        return map;
    }

    public static Connection getConnection() throws Exception {
        String dbUrl = System.getenv("JDBC_DATABASE_URL");
        if (dbUrl != null && dbUrl.length() > 0) {
            return DriverManager.getConnection(dbUrl);
        }

        return DriverManager.getConnection("jdbc:sqlite:demo.db");
    }
}
