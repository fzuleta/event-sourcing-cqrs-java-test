import {inject} from 'aurelia-framework';
import {EventAggregator} from 'aurelia-event-aggregator';
import {apigateway} from './connection/apigateway';
import {trace} from './common/functions';

@inject(EventAggregator, apigateway)
export class App {
    betAmount:String;
    winResult:String = "";
    connectionMessage:String="Connecting to server";
    connecting:boolean = false;
    connectionSucceded:boolean = false;
    apigateway:apigateway;
    eventAggregator:EventAggregator;
    user={};
    balance={};

    constructor(EventAggregator, apigateway){
        this.betAmount = "1000";
        this.connecting = false;
        this.apigateway = apigateway;
        this.eventAggregator = EventAggregator;
    }
    activate(){
        return new Promise((resolve,reject)=>{ 
            this.apigateway.doApiCall({}, "/getuser/")
            .then(o=>{
                this.connectionSucceded = true;
                this.user = o["data"];
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
            userId: this.user["id"],
            betAmount: this.betAmount
        };
        this.apigateway.doApiCall(data, "/play/").then(o=>{
            this.connecting = false;
            if (o["success"]) {
                const data = o["data"];
                this.balance["gold"] = data.balance;
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
