import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : ComponentActivity() {
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firestore = FirebaseFirestore.getInstance()
        setContent {
            MaterialTheme {
                Formulario()
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Preview
    @Composable
    fun Formulario() {
        var nome by remember { mutableStateOf(TextFieldValue()) }
        var endereco by remember { mutableStateOf(TextFieldValue()) }
        var bairro by remember { mutableStateOf(TextFieldValue()) }
        var cep by remember { mutableStateOf(TextFieldValue()) }
        var cidade by remember { mutableStateOf(TextFieldValue()) }
        var estado by remember { mutableStateOf(TextFieldValue()) }
        var dadosLidos by remember { mutableStateOf("") }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "CADASTRANDO DADOS")

            Spacer(modifier = Modifier.height(20.dp))

            TextField(
                value = nome,
                onValueChange = { nome = it },
                label = { Text("Nome") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(20.dp))

            TextField(
                value = endereco,
                onValueChange = { endereco = it },
                label = { Text("Endereço") },
                modifier = Modifier.fillMaxWidth()
            )



            Spacer(modifier = Modifier.height(20.dp))

            TextField(
                value = bairro,
                onValueChange = { bairro = it },
                label = { Text("Bairro") },
                modifier = Modifier.fillMaxWidth()
            )



            Spacer(modifier = Modifier.height(20.dp))

            TextField(
                value = cep,
                onValueChange = { cep = it },
                label = { Text("CEP") },
                modifier = Modifier.fillMaxWidth()
            )



            Spacer(modifier = Modifier.height(20.dp))

            TextField(
                value = cidade,
                onValueChange = { cidade = it },
                label = { Text("Cidade") },
                modifier = Modifier.fillMaxWidth()
            )


            Spacer(modifier = Modifier.height(20.dp))

            TextField(
                value = estado,
                onValueChange = { estado = it },
                label = { Text("Estado") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    val data = hashMapOf(
                        "nome" to nome.text,
                        "endereco" to endereco.text,
                        "bairro" to bairro.text,
                        "cep" to cep.text,
                        "cidade" to cidade.text,
                        "estado" to estado.text
                    )

                    firestore.collection("formulario")
                        .add(data)
                        .addOnSuccessListener {
                            nome = TextFieldValue()
                            endereco = TextFieldValue()
                            bairro = TextFieldValue()
                            cep = TextFieldValue()
                            cidade = TextFieldValue()
                            estado = TextFieldValue()
                        }
                        .addOnFailureListener { e ->
                            // Handle failure
                        }
                },
                modifier = Modifier.padding(bottom = 150.dp)
            ) {
                Text("Cadastrar Dados")
            }

            Button(
                onClick = {
                    lerDadosDoFirebase { dados ->
                        dadosLidos = dados
                    }
                },
                modifier = Modifier.padding(bottom = 150.dp)
            ) {
                Text("Ler Dados")
            }

            Text(
                text = dadosLidos,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }
    }

    private fun lerDadosDoFirebase(callback: (String) -> Unit) {


        firestore.collection("formulario")
            .get()
            .addOnSuccessListener { documents ->
                val builder = StringBuilder()
                for (document in documents) {
                    val nome = document.getString("nome")
                    val endereco = document.getString("endereco")
                    val bairro = document.getString("bairro")
                    val cep = document.getString("cep")
                    val cidade = document.getString("cidade")
                    val estado = document.getString("estado")


                    builder.append("$nome, $endereco, $bairro, $cep, $cidade, $estado\n")
                }


                val dadosLidos = builder.toString()
                callback(dadosLidos)

            }
            .addOnFailureListener { e ->

            }
    }
}
