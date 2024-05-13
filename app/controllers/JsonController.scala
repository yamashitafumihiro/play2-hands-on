package controllers

import play.api.mvc.{AbstractController, ControllerComponents}

import javax.inject.Inject

class JsonController @Inject()(components: ControllerComponents)
  extends AbstractController(components) {

  def list = TODO
  def create = TODO
  def update = TODO
  def remove(id: Long) = TODO
}
