package it.utiu.thesis.stream

import akka.actor.Props
import it.utiu.thesis.base.AbstractConsumerActor
import it.utiu.thesis.utils.Costants


object DiabetesConsumerActor {
  val header = ""
  private val COLS_NUM = 21

  def props(): Props = Props(new DiabetesConsumerActor())
}

class DiabetesConsumerActor extends AbstractConsumerActor(Costants.TOPIC_DIABETES, DiabetesConsumerActor.header) {
  override def isPredictionRequest(row: String): Boolean = row.split(",").length == (DiabetesConsumerActor.COLS_NUM - 1)
}

