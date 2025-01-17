package meteor
package scanamo
package formats

import meteor.codec.{Decoder, Encoder}
import meteor.errors.DecoderError
import org.scanamo.{DynamoFormat, DynamoReadError}
import software.amazon.awssdk.services.dynamodb.model.AttributeValue

object conversions {

  implicit def dynamoFormatToDecoder[T](implicit
  df: DynamoFormat[T]): Decoder[T] =
    new Decoder[T] {
      def read(av: AttributeValue): Either[DecoderError, T] =
        df.read(av).left.map { err =>
          DecoderError(DynamoReadError.describe(err), None)
        }
    }

  implicit def dynamoFormatToEncoder[T](implicit
  df: DynamoFormat[T]): Encoder[T] =
    new Encoder[T] {
      def write(a: T): AttributeValue =
        df.write(a).toAttributeValue
    }
}
