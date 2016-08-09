package repositories.interfaces

case class Errors(list: List[String]){
  def +(errors: Errors) = Errors(list ++ errors.list)
  def isEmpty = list.isEmpty
}

object Errors {
  def single(info: String) = Errors(List(info))
  def empty = Errors(List.empty)
}
