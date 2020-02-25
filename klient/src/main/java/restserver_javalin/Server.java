package restserver_javalin;

import brugerautorisation.data.Bruger;
import brugerautorisation.transport.rmi.Brugeradmin;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.rmi.Naming;

public class Server {
    public static Javalin app;


    public static void main(String[] args) throws Exception {
        start();
    }

    public static void stop() {
        app.stop();
        app = null;
    }

    public static void start() throws Exception {
        if (app!=null) return;

        app = Javalin.create().start(8080);
        app.before(ctx -> {
            System.out.println("Javalin Server fik "+ctx.method()+" på " +ctx.url()+ " med query "+ctx.queryParamMap()+ " og form " +ctx.formParamMap());
        });
        app.exception(Exception.class, (e, ctx) -> {
            e.printStackTrace();
        });
        app.get("/", ctx -> ctx.contentType("text/html; charset=utf-8")
                .result("<html><body>Velkommen.<br/>\n<br/>\n" +
                                "Du kan sige <a href='hej'>hej</a> eller udfylde en <a href='formular'>formular</a><br/>\n<br/>\n" +
                                "Du kunne også spørge til <a href='bruger/s123456'>en bruger</a>" +
                                "... eventuelt med <a href='bruger/s123456?adgangskode=kode1xyz'>adgangskode</a><br/>\n<br/>\n" +
                                "Eller sendGlemtAdgangskodeEmail til <form method=post action=sendGlemtAdgangskodeEmail>" +
                                "brugernavn <input name=brugernavn type=text> med følgetekst: <input name=foelgetekst type=text value='Hej fra Javalin'><input type=submit></form></form>"
                        ));
        app.get("/hej", ctx -> ctx.result("Hejsa, godt at møde dig!"));
        app.get("/formular", ctx -> formular(ctx));

        app.get("/bruger/:brugernavn", ctx -> bruger(ctx));
        app.post("/sendGlemtAdgangskodeEmail", ctx -> sendGlemtAdgangskodeEmail(ctx));
        app.post("/bruger/:brugernavn/sendGlemtAdgangskodeEmail", ctx -> sendGlemtAdgangskodeEmail(ctx));
    }

    private static void formular(Context ctx) {
        String fornavn = ctx.queryParam("fornavn");
        if (fornavn==null) {
            ctx.contentType("text/html; charset=utf-8").result("<html><body><form method=get>Skriv dit fornavn: <input name=fornavn type=text></form></html>");
        } else {
            ctx.contentType("text/html; charset=utf-8").result("<html><body>Hej "+fornavn+", godt at møde dig!</html>");
        }
    }

    private static void bruger(Context ctx) throws Exception {
        String brugernavn = ctx.pathParam("brugernavn");    // del af path  /bruger/s123456
        String adgangskode = ctx.queryParam("adgangskode"); // del af query  ?adgangskode=kode1xyz
        Brugeradmin ba = (Brugeradmin) Naming.lookup("rmi://javabog.dk/brugeradmin");
        if (adgangskode==null) {
            Bruger bruger = ba.hentBrugerOffentligt(brugernavn);
            ctx.json(bruger);
        } else try {
            Bruger bruger = ba.hentBruger(brugernavn, adgangskode);
            ctx.json(bruger);
        } catch (Exception e) {
            ctx.status(401).result("Unauthorized");
        }
    }

    private static void sendGlemtAdgangskodeEmail(Context ctx) throws Exception {
        Brugeradmin ba = (Brugeradmin) Naming.lookup("rmi://javabog.dk/brugeradmin");
        String brugernavn = ctx.formParam("brugernavn");
        String følgetekst = ctx.formParam("foelgetekst");
        if (brugernavn==null) brugernavn = ctx.queryParam("brugernavn");
        ba.sendGlemtAdgangskodeEmail(brugernavn, følgetekst);
    }

}
