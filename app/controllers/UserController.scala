package controllers

import models.Users
import play.api.mvc.{MessagesAbstractController, MessagesControllerComponents}
import scalikejdbc.{DB, select, withSQL}

import javax.inject.Inject

class UserController @Inject()(components: MessagesControllerComponents) extends MessagesAbstractController(components){
  def list = Action { implicit request =>
    val u = Users.syntax("u")

    DB.readOnly { implicit session =>
      val users = withSQL {
        select.from(Users as u).orderBy(u.id.asc)
      }.map(Users(u.resultName)).list.apply()

      Ok(views.html.user.list(users))
    }
  }
  def edit(id: Option[Long]) = TODO
  def create = TODO
  def update = TODO
  def remove(id: Long) = TODO
}
