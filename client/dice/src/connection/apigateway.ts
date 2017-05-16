import { inject } from 'aurelia-framework';
import {HttpClient as httpFetch, json} from 'aurelia-fetch-client';
import {EventAggregator} from 'aurelia-event-aggregator';
import {trace} from './../common/functions';

@inject('apiRoot', EventAggregator,httpFetch)
export class apigateway {
    apiRoot:string;
    eventAggregator;
    httpFetch;


    constructor(apiRoot,EventAggregator,httpFetch){
        this.apiRoot = apiRoot;
        this.eventAggregator = EventAggregator; 

        httpFetch.configure(config => {
            config
                .withDefaults({
                    credentials: 'include', // Valid values; omit, same-origin and include
                    headers: {
                        'Accept': 'application/json',
                        'X-Requested-With': 'Fetch'
                    }
                })
                .withInterceptor({
                    request(request) {
                        trace(`Requesting ${request.method} ${request.url}`); 
                        return request; 
                    },
                    // response(response) {
                    //     console.log(`Received ${response.status} ${response.url}`);
                    //     return response; // you can return a modified Response
                    // }
                });
        });
        this.httpFetch = httpFetch;

    }

    doApiCall(myPostData={}, endPoint="", isFormData=false) {
        let body = null;
        myPostData = Object.assign({}, myPostData);
        if (!isFormData) { body = JSON.stringify(myPostData) } else { body = myPostData; }

        return new Promise( (resolve, reject) => { 
            this.httpFetch.fetch(this.apiRoot + endPoint, {
                method: "POST",
                body: body
            })
            .then(response => response.json())
            .then(  o => resolve(o))
            .catch( o => reject(o)); 
        });
    }
}
