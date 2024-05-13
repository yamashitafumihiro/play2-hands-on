package controllers

import models.Users
import play.api.libs.functional.syntax.{toFunctionalBuilderOps, unlift}
import play.api.libs.json.{Json, Writes, __}
import play.api.mvc.{AbstractController, ControllerComponents}
import scalikejdbc.{DB, select, withSQL}
import JsonController._

import javax.inject.Inject

class JsonController @Inject()(components: ControllerComponents)
  extends AbstractController(components) {

  def list = Action { implicit request =>
    val u = Users.syntax("u")

    DB.readOnly { implicit session =>
      val users = withSQL {
        select.from(Users as u).orderBy(u.id.asc)
      }.map(Users(u.resultName)).list.apply()

      Ok(Json.obj("users" -> users))
    }
  }
  def create = TODO
  def update = TODO
  def remove(id: Long) = TODO
}

object JsonController {
  // UsersをJSONに変換するためのWritesを定義
  implicit val usersWrites: Writes[Users] = (
    (__ \ "id"       ).write[Long]   and
    (__ \ "name"     ).write[String] and
    (__ \ "companyId").writeNullable[Int]
    )(unlift(Users.unapply))
}