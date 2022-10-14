package com.chanoktrue.tcpsocketdemo

import android.content.Context
import android.os.Bundle
import android.provider.SyncStateContract.Constants
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import com.chanoktrue.tcpsocketdemo.ui.theme.TCPSocketDemoTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TCPSocketDemoTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {

                    val context: Context = LocalContext.current
                    Shared.context = context

                    TCPSocketView()

                }
            }
        }
    }
}

@Composable
fun TCPSocketView() {



    var write by remember { mutableStateOf("") }
    var read by remember { mutableStateOf("") }

  Column(
      modifier = Modifier.fillMaxSize(),
      horizontalAlignment = Alignment.CenterHorizontally,
//      verticalArrangement = Arrangement.SpaceAround
  ) {

      //Button Connect
      OutlinedButton(
          onClick = {
              CoroutineScope(Dispatchers.IO).launch {
                TCPSocket.shared().connect {
                    read = it
                }
              }
          },
          modifier = Modifier.fillMaxWidth().padding(10.dp)
      ) {
          Text("Connect")
      }


      //Read
      TextField(
          value = read,
          onValueChange = {},
          modifier = Modifier
              .fillMaxWidth()
              .fillMaxHeight(0.5f)
              .padding(10.dp)
      )

      //Write
      TextField(
          value = write,
          onValueChange = {write = it},
          modifier = Modifier
              .fillMaxWidth()
              .padding(10.dp)
      )

      //Button Send
      OutlinedButton(
          onClick = {
              CoroutineScope(Dispatchers.IO).launch {
                  TCPSocket.shared().write(write) {
                     println(it)
                      CoroutineScope(Dispatchers.Main).launch {
                          if (it) {
                              Toast.makeText(Shared.context, "Send complete", Toast.LENGTH_LONG).show()
                          }else{
                              Toast.makeText(Shared.context, "Send error", Toast.LENGTH_LONG).show()
                          }
                      }
                  }
              }
          },
          modifier = Modifier.fillMaxWidth().padding(10.dp)
      ) {
          Text("Send Message")
      }

      //Button Disconnect
      OutlinedButton(
          onClick = {
              CoroutineScope(Dispatchers.IO).launch {
                  TCPSocket.shared().disconnect()
              }
          },
          modifier = Modifier.fillMaxWidth().padding(10.dp)
      ) {
          Text("Disconnect")
      }

  }
}

@Preview(showSystemUi = true)
@Composable
fun DefaultPreview() {
    TCPSocketDemoTheme {
        TCPSocketView()
    }
}