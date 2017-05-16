import {inject} from 'aurelia-framework';
import {EventAggregator} from 'aurelia-event-aggregator';
import {apigateway} from './connection/apigateway';
import {trace} from './common/functions';

@inject(EventAggregator, apigateway)
export class App {
    balance:String;
    userId:String;
    betAmount:String;
    winResult:String = "";
    connectionMessage:String="Connecting to server";
    connecting:boolean = false;
    connectionSucceded:boolean = false;
    apigateway:apigateway;
    eventAggregator:EventAggregator;

    constructor(EventAggregator, apigateway){
        this.userId = "0";
        this.betAmount = "1000";
        this.connecting = false;
        this.apigateway = apigateway;
        this.eventAggregator = EventAggregator;
    }
    activate(){
        return new Promise((resolve,reject)=>{ 
            this.apigateway.doApiCall({userId:this.userId}, "/getbalance/")
            .then(o=>{
                this.connectionSucceded = true;
                this.balance = o["data"].balance;
                resolve();
            })
            .catch(o=>{
                this.connectionSucceded = false;
                this.connectionMessage = "Can't connect to server, is it running?";
                resolve(o);
            });
        });
    }

    play(){
        this.connecting = true;
        this.winResult = "";
        const data = {
            userId: this.userId,
            betAmount: this.betAmount
        };
        this.apigateway.doApiCall(data, "/play/").then(o=>{
            this.connecting = false;
            if (o["success"]) {
                const data = o["data"];
                this.balance = data.balance;
                if (data.winAmount){
                    this.winResult = "Win! $" + data.winAmount;
                } else {
                    this.winResult = "No Win";
                }
            } else {
                this.winResult = "Error";
            }
        })
    }
}
