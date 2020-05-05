package restserver_javalin;

import brugerautorisation.data.Bruger;
import brugerautorisation.transport.rmi.Brugeradmin;
import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.plugin.openapi.OpenApiOptions;
import io.javalin.plugin.openapi.OpenApiPlugin;
import io.javalin.plugin.openapi.ui.ReDocOptions;
import io.javalin.plugin.openapi.ui.SwaggerOptions;
import io.swagger.v3.oas.models.info.Info;

import java.io.File;
import java.io.FileInputStream;
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

    private static OpenApiPlugin getConfiguredOpenApiPlugin() {
        Info info = new Info().version("1.0").title("User API").description("Demo API with 5 operations");
        OpenApiOptions options = new OpenApiOptions(info)
                .activateAnnotationScanningFor("javalin_resources.HttpMethods")
                .activateAnnotationScanningFor("io.javalin.example.java")
                .path("/swagger-docs") // endpoint for OpenAPI json
                .swagger(new SwaggerOptions("/swagger-ui")) // endpoint for swagger-ui
                .reDoc(new ReDocOptions("/redoc")) // endpoint for redoc
                .defaultDocumentation(doc -> {
//                    doc.json("500", Bruger.class);
                });
        return new OpenApiPlugin(options);
    }

    public static void start() throws Exception {
        if (app!=null) return;

        app = Javalin.create(
                config -> config.enableCorsForAllOrigins()
                .registerPlugin(getConfiguredOpenApiPlugin())
        );
        app.before(ctx -> {
            System.out.println("Javalin Server fik "+ctx.method()+" på " +ctx.url()+ " med query "+ctx.queryParamMap()+ " og form " +ctx.formParamMap());
        });
        /*
        app.exception(Exception.class, (e, ctx) -> {
            e.printStackTrace();
            ctx.status((HttpStatus.INTERNAL_SERVER_ERROR_500); // husk at sætte status - ellers giver javalin 200 Found tilbage.
        });
         */
        app.config.addStaticFiles("webside");
//        app.config.registerPlugin(getConfiguredOpenApiPlugin());

        // Serverside gemererede websider
        app.get("/prut", ctx -> ctx.status(404).result("Ups, der kom en...!").contentType("text/html"));
        app.get("/formular", ctx -> formular(ctx));

        // REST endpoints
//        app.config.enableCorsForAllOrigins();
        app.get("/rest/hej", ctx -> ctx.result("Hejsa, godt at møde dig!"+new File(".").getAbsolutePath()));
        app.get("/rest/billede", ctx -> ctx.result(new FileInputStream("billede.jpg")).contentType("image/jpg"));
        app.get("/rest/hej/:fornavn", ctx -> ctx.result("Hej "+ctx.pathParam("fornavn")+", godt at møde dig!"));
        app.get("/rest/bruger/:brugernavn", ctx -> bruger(ctx));
        app.post("/rest/sendGlemtAdgangskodeEmail", ctx -> sendGlemtAdgangskodeEmail(ctx));

        app.start(8081);
        System.err.println();
        System.err.println("Prøv http://localhost:8081/swagger-docs/");
        System.err.println("Prøv http://localhost:8081/swagger-ui#/");
        System.err.println("Prøv http://localhost:8081/redoc/");
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
        ctx.result("Der blev sendt en mail til "+brugernavn+" med teksten "+følgetekst);
    }

}
