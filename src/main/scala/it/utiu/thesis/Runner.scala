package it.utiu.thesis

import akka.actor.{ActorRef, ActorSystem}
import it.utiu.thesis.base.{AbstractConsumerActor, AbstractProducerActor, AbstractTrainerActor}
import it.utiu.thesis.machinelearing.{DiabetesPredictorActor, DiabetesTrainerActor}
import it.utiu.thesis.stream.{DiabetesConsumerActor, DiabetesProducerActor}

import java.util.Date
import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.{Duration, DurationInt}
import scala.language.postfixOps

object Runner {

  def main(args: Array[String]): Unit = {
    //create akka system
    val system = ActorSystem("diabetes")
    println("Starting DIABETES at " + new Date() + "...")
    val app = new Runner(system)
    app.run()
  }
}

class Runner(system: ActorSystem) {
  //define actors
  private var trainerRef: ActorRef = _
  private var predictorRef: ActorRef = _
  private var consumerRef: ActorRef = _
  private var producerRef: ActorRef = _
  private var feederRef: ActorRef = _

  def run(): Unit = {

    //creazione degli attori
    trainerRef = system.actorOf(DiabetesTrainerActor.props(), "trainer-activity")
    predictorRef = system.actorOf(DiabetesPredictorActor.props(), "predictor-activity")
    consumerRef = system.actorOf(DiabetesConsumerActor.props(), "consumer-activity")
    producerRef = system.actorOf(DiabetesProducerActor.props(), "producer-activity")
    feederRef = system.actorOf(DiabetesProducerActor.props(), "feeder-activity")

    //partenza degli attori
    Thread.sleep(2000)
    //consumer
    consumerRef ! AbstractConsumerActor.StartConsuming()
    Thread.sleep(2000)
    //producer
    producerRef ! AbstractProducerActor.StartProducing()
    Thread.sleep(2000)
    system.scheduler.scheduleOnce(1 minute) {
      //trainer
      trainerRef ! AbstractTrainerActor.StartTraining()
    }

    Await.ready(system.whenTerminated, Duration.Inf)
  }
}
