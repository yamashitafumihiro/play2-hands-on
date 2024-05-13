package controllers

import models.Users
import play.api.libs.functional.syntax.{toFunctionalBuilderOps, unlift}
import play.api.libs.json.{JsError, Json, Reads, Writes, __}
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
  def create = Action(parse.json) { implicit request =>
    request.body.validate[UserForm].map{ form =>
      DB.localTx { implicit session =>
        Users.create(form.name, form.companyId)
        Ok(Json.obj("result" -> "success"))
      }
    }.recoverTotal { e =>
      BadRequest(Json.obj("result" -> "failure", "error" -> JsError.toJson(e)))
    }
  }
  def update = Action(parse.json) { implicit request =>
    request.body.validate[UserForm].map{ form =>
      DB.localTx { implicit session =>
        Users.find(form.id.get).foreach { user =>
          Users.save(user.copy(name = form.name, companyId = form.companyId))
        }
        Ok(Json.obj("result" -> "success"))
      }
    }.recoverTotal { e =>
      BadRequest(Json.obj("result" -> "failure", "error" -> JsError.toJson(e)))
    }
  }
  def remove(id: Long) = TODO
}

object JsonController {
  // UsersをJSONに変換するためのWritesを定義
  implicit val usersWrites: Writes[Users] = (
    (__ \ "id"       ).write[Long]   and
    (__ \ "name"     ).write[String] and
    (__ \ "companyId").writeNullable[Int]
    )(unlift(Users.unapply))

  case class UserForm(id: Option[Long], name: String, companyId: Option[Int])

  implicit val userFormReads: Reads[UserForm] = (
    (__ \ "id").readNullable[Long] and
    (__ \ "name").read[String] and
    (__ \ "companyId").readNullable[Int]
    )(UserForm)
}