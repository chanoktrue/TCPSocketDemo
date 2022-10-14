package com.chanoktrue.tcpsocketdemo

import android.provider.Contacts.Intents.UI
import android.util.Log
import java.net.Socket
import java.util.Scanner

class TCPSocket {

    //Singleton Class
    companion object {
        private  var obj: TCPSocket? = null
        fun shared(): TCPSocket {
            if (obj == null) {
                obj = TCPSocket()
            }
            return obj as TCPSocket
        }
    }

    var socket: Socket = Socket()
    val address: String = "192.168.9.97"
    var port: Int = 5000
    var isConnect: Boolean = false

    suspend fun connect(completion: (String) -> Unit) {
        try {
            if (isConnect == false) {
                socket = Socket(address, port)
            }
            Log.e("Socket connect", "address: $address port: $port")
            isConnect = true
            read(completion)
        }catch (e: Exception) {
            Log.e("Connect error:", e.toString())
            isConnect = false
            socket.close()
        }
    }

    suspend fun read(completion: (String) -> Unit) {
        val read = Scanner(socket.getInputStream())
        while (isConnect) {
            var msg = read.nextLine()
            completion(msg)
        }
    }

    suspend fun write(msg: String, completion: (Boolean) -> Unit) {
        if (isConnect == false) {
            completion(false)
            return
        }
        var write = socket.getOutputStream()
        write.write(msg.toByteArray())
        completion(true)
    }

    suspend fun disconnect() {
        isConnect = false
        socket.close()
        Log.e("Socket", "Disconnect")
    }

}