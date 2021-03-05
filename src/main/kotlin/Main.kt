import com.google.ortools.algorithms.KnapsackSolver
import com.google.ortools.algorithms.operations_research_algorithmsJNI
import net.troja.eve.esi.ApiClientBuilder
import net.troja.eve.esi.api.CharacterApi
import net.troja.eve.esi.api.ContractsApi
import net.troja.eve.esi.auth.OAuth
import net.troja.eve.esi.auth.SsoScopes
import net.troja.eve.esi.model.CorporationContractsResponse
import opta.Ingot
import opta.Knapsack
import opta.KnapsackSolution
import org.optaplanner.core.api.solver.SolverFactory
import org.optaplanner.core.api.solver.SolverJob
import org.optaplanner.core.api.solver.SolverManager
import org.optaplanner.core.config.solver.SolverConfig
import org.optaplanner.core.config.solver.SolverManagerConfig
import java.awt.Desktop
import java.math.BigDecimal
import java.net.URI
import java.time.OffsetDateTime
import java.util.*
import java.util.concurrent.ExecutionException
import java.util.concurrent.Executors
import java.util.concurrent.ThreadPoolExecutor
import kotlin.math.roundToInt
import kotlin.math.roundToLong
import java.util.ArrayList





class Main

val CLIENT_ID = "xx"
val SECRET_KEY = "xx"
val SSO_CALLBACK_URL = "http://localhost:8080/api/auth"
val REFRESH_TOKEN = "xxxxx"
const val DATASOURCE = "tranquility"
val MAX_CAP = 345000

fun doAuth() {
    val client = ApiClientBuilder().clientID(CLIENT_ID).build()
    val auth: OAuth = client.getAuthentication("evesso") as OAuth
    val scopes = setOf(SsoScopes.ESI_CONTRACTS_READ_CORPORATION_CONTRACTS_V1)
    val authUrl = auth.getAuthorizationUri(SSO_CALLBACK_URL, scopes, "abajksd")
    Desktop.getDesktop().browse(URI(authUrl))

}

fun finishAuth() {
    val client = ApiClientBuilder().clientID(CLIENT_ID).build()
    val auth: OAuth = client.getAuthentication("evesso") as OAuth
    val scopes = setOf(SsoScopes.ESI_CONTRACTS_READ_CORPORATION_CONTRACTS_V1)
    auth.getAuthorizationUri(SSO_CALLBACK_URL, scopes, "abajksd")
    auth.finishFlow("xxxxxxxxx", "abajksd")
    val refreshToken = auth.refreshToken
    println(refreshToken)
}

fun callEndpoints() {
    val ONE_HUNDRED = BigDecimal(100)
    val client = ApiClientBuilder().clientID(CLIENT_ID).refreshToken(REFRESH_TOKEN).build()
    val api = ContractsApi().also { it.apiClient = client }
    val char = CharacterApi().also { it.apiClient = client }
    //val x = char.getCharactersCharacterId(92901161, DATASOURCE, null)
    val x = api.getCorporationsCorporationIdContracts(98209548, DATASOURCE, null, null, null)
        .filter { it.status == CorporationContractsResponse.StatusEnum.OUTSTANDING }
        .filter { it.type == CorporationContractsResponse.TypeEnum.COURIER }
        .filter { it.dateExpired.isAfter(OffsetDateTime.now()) }
        .filter { it.startLocationId == 1032110505696 }
        .filter { it.endLocationId == 1034323745897}
        .toTypedArray()

    val ingots = x.mapIndexed() { i,x ->  Ingot().also {
        it.id = i
        it.value = (x.reward!!/1000.0).roundToInt()
        it.weight = x.volume!!.roundToInt()
    } }

    val problem = KnapsackSolution().also {
        it.ingots = ingots
        it.knapsack = Knapsack().also { it.maxWeight = 340000 }
    }

    val solverFactory = SolverFactory.createFromXmlResource<KnapsackSolution>("solverConfig.xml")

    val solver = solverFactory.buildSolver()


    val solution: KnapsackSolution = try {
        // Wait until the solving ends
        solver.addEventListener {
            val selected = it.newBestSolution.ingots!!.filter { it.selected!! }
            val volume = selected.sumBy { it.weight }
            val reward = selected.sumBy { it.value }
            println("------------------")
            println("Volume: ${volume}m³")
            println("Reward: ${reward/ 1000.0} mio isk")
            println("Contracts:")
            selected.forEach {
                println("${it.weight} | ${it.value}")
            }
            println("------------------")
        }
        solver.solve(problem)
    } catch (e: InterruptedException) {
        throw IllegalStateException("Solving failed.", e)
    } catch (e: ExecutionException) {
        throw IllegalStateException("Solving failed.", e)
    }
    println(solution)
    println(solution.ingots)

    val selected = solution.ingots!!.filter { it.selected!! }
    val volume = selected.sumBy { it.weight }
    val reward = selected.sumBy { it.value }

    println("------------------")
    println("Volume: ${volume}m³")
    println("Reward: ${reward/ 1000.0} mio isk")
    println("Contracts:")
    selected.forEach {
        println("${it.weight} | ${it.value}")
    }
    println("------------------")
    System.exit(0)

    /*
Volume: 294101m³
Reward: 267.904 mio isk
Contracts:
213695 | 174468
77430 | 70000
2976 | 23436



Volume: 337021m³
Reward: 363.836 mio isk
Contracts:
244908 | 260400
89137 | 80000
2976 | 23436

Volume: 337021m³
Reward: 363.836 mio isk
Contracts:
244908 | 260400
89137 | 80000
2976 | 23436
     */
}


fun main() {
//    doAuth()
//    finishAuth()
    callEndpoints()

//    val app = Javalin.create().start(8080)
//    app.get("/") { ctx ->
//        ctx.result("Hello World")
//    }
}