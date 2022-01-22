package snake

import java.net.Socket
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

final case class ConnectedClient(sock:Socket, ois:ObjectInputStream, oos:ObjectOutputStream, player:SnakeHead)