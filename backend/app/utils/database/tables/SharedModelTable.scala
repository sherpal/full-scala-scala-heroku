package utils.database.tables

import models.SharedModelClass
import slick.lifted.Tag
import slick.jdbc.PostgresProfile.api._

final class SharedModelTable(tag: Tag) extends Table[SharedModelClass](tag, "shared_model") {

  def foo = column[String]("foo", O.PrimaryKey)
  def bar = column[Int]("bar")

  def * = (foo, bar) <> (SharedModelClass.tupled, SharedModelClass.unapply)

}

object SharedModelTable {

  def query: TableQuery[SharedModelTable] = TableQuery[SharedModelTable]

}
