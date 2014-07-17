object LiveTriples {
  import Kernel.ServiceWrapper.runService

  def main(args: Array[String]) = args.toList match {
    case host :: port :: Nil if (port.forall(_.isDigit)) => runService(host, port.toInt)
    case Nil => runService()
    case _ => println("LiveTriples <host> <port>")
  }
}
