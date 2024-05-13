package controllers

import controllers.UserController.{UserForm, userForm}
import models.{Companies, Users}
import play.api.data.Form
import play.api.data.Forms.{longNumber, mapping, nonEmptyText, number, optional}
import play.api.mvc.{MessagesAbstractController, MessagesControllerComponents}
import scalikejdbc.{DB, select, withSQL}

import javax.inject.Inject

class UserController @Inject()(components: MessagesControllerComponents) extends MessagesAbstractController(components){
  private val u = Users.syntax("u")
  private val c = Companies.syntax("c")
  def list = Action { implicit request =>

    DB.readOnly { implicit session =>
      val users = withSQL {
        select.from(Users as u).orderBy(u.id.asc)
      }.map(Users(u.resultName)).list.apply()

      Ok(views.html.user.list(users))
    }
  }
  def edit(id: Option[Long]) = Action { implicit request =>
    DB.readOnly { implicit session =>
      val form = id match {
        case None => userForm
        case Some(id) => Users.find(id) match {
          case Some(user) => userForm.fill(UserForm(Some(user.id), user.name, user.companyId))
          case None => userForm
        }
      }

      val companies = withSQL {
        select.from(Companies as c).orderBy(c.id.asc)
      }.map(Companies(c.resultName)).list().apply()

      Ok(views.html.user.edit(form, companies))
    }
  }
  def create = TODO
  def update = TODO
  def remove(id: Long) = TODO
}

object UserController {
  case class UserForm(id: Option[Long], name: String, companyId: Option[Int])

  val userForm = Form(
    mapping(
      "id" -> optional(longNumber),
      "name" -> nonEmptyText(maxLength = 20),
      "companyId" -> optional(number)
    )(UserForm.apply)(UserForm.unapply)
  )
}