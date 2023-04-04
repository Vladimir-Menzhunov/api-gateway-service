package common

import sttp.tapir.{Codec, CodecFormat}

object JsonProtocol {
  implicit val codecRequestForOneService: Codec[String, RequestForOneService, CodecFormat.TextPlain] =
    Codec.string.map(str => RequestForOneService(str))(_.requestStr)
}
