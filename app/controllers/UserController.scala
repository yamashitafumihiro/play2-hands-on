package controllers

import play.api.mvc.{MessagesAbstractController, MessagesControllerComponents}

import javax.inject.Inject

class UserController @Inject()(components: MessagesControllerComponents) extends MessagesAbstractController(components){
  def list = TODO
  def edit(id: Option[Long]) = TODO
  def create = TODO
  def update = TODO
  def remove(id: Long) = TODO
}
