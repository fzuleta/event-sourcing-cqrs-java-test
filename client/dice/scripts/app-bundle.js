var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};
define('app',["require", "exports", "aurelia-framework", "aurelia-event-aggregator", "./connection/apigateway"], function (require, exports, aurelia_framework_1, aurelia_event_aggregator_1, apigateway_1) {
    "use strict";
    Object.defineProperty(exports, "__esModule", { value: true });
    var App = (function () {
        function App(EventAggregator, apigateway) {
            this.winResult = "";
            this.connectionMessage = "Connecting to server";
            this.connecting = false;
            this.connectionSucceded = false;
            this.user = {};
            this.balance = {};
            this.betAmount = "1000";
            this.connecting = false;
            this.apigateway = apigateway;
            this.eventAggregator = EventAggregator;
        }
        App.prototype.activate = function () {
            var _this = this;
            return new Promise(function (resolve, reject) {
                _this.apigateway.doApiCall({}, "/getuser/")
                    .then(function (o) {
                    _this.connectionSucceded = true;
                    _this.user = o["data"];
                    _this.balance = o["data"].balance;
                    resolve();
                })
                    .catch(function (o) {
                    _this.connectionSucceded = false;
                    _this.connectionMessage = "Can't connect to server, is it running?";
                    resolve(o);
                });
            });
        };
        App.prototype.play = function () {
            var _this = this;
            this.connecting = true;
            this.winResult = "";
            var data = {
                userId: this.user["id"],
                betAmount: this.betAmount
            };
            this.apigateway.doApiCall(data, "/play/").then(function (o) {
                _this.connecting = false;
                if (o["success"]) {
                    var data_1 = o["data"];
                    _this.balance["gold"] = data_1.balance;
                    if (data_1.winAmount) {
                        _this.winResult = "Win! $" + data_1.winAmount;
                    }
                    else {
                        _this.winResult = "No Win";
                    }
                }
                else {
                    _this.winResult = "Error";
                }
            });
        };
        return App;
    }());
    App = __decorate([
        aurelia_framework_1.inject(aurelia_event_aggregator_1.EventAggregator, apigateway_1.apigateway),
        __metadata("design:paramtypes", [Object, Object])
    ], App);
    exports.App = App;
});

//# sourceMappingURL=data:application/json;charset=utf8;base64,eyJ2ZXJzaW9uIjozLCJzb3VyY2VzIjpbImFwcC50cyJdLCJuYW1lcyI6W10sIm1hcHBpbmdzIjoiOzs7Ozs7Ozs7Ozs7SUFNQSxJQUFhLEdBQUc7UUFXWixhQUFZLGVBQWUsRUFBRSxVQUFVO1lBVHZDLGNBQVMsR0FBVSxFQUFFLENBQUM7WUFDdEIsc0JBQWlCLEdBQVEsc0JBQXNCLENBQUM7WUFDaEQsZUFBVSxHQUFXLEtBQUssQ0FBQztZQUMzQix1QkFBa0IsR0FBVyxLQUFLLENBQUM7WUFHbkMsU0FBSSxHQUFDLEVBQUUsQ0FBQztZQUNSLFlBQU8sR0FBQyxFQUFFLENBQUM7WUFHUCxJQUFJLENBQUMsU0FBUyxHQUFHLE1BQU0sQ0FBQztZQUN4QixJQUFJLENBQUMsVUFBVSxHQUFHLEtBQUssQ0FBQztZQUN4QixJQUFJLENBQUMsVUFBVSxHQUFHLFVBQVUsQ0FBQztZQUM3QixJQUFJLENBQUMsZUFBZSxHQUFHLGVBQWUsQ0FBQztRQUMzQyxDQUFDO1FBQ0Qsc0JBQVEsR0FBUjtZQUFBLGlCQWVDO1lBZEcsTUFBTSxDQUFDLElBQUksT0FBTyxDQUFDLFVBQUMsT0FBTyxFQUFDLE1BQU07Z0JBQzlCLEtBQUksQ0FBQyxVQUFVLENBQUMsU0FBUyxDQUFDLEVBQUUsRUFBRSxXQUFXLENBQUM7cUJBQ3pDLElBQUksQ0FBQyxVQUFBLENBQUM7b0JBQ0gsS0FBSSxDQUFDLGtCQUFrQixHQUFHLElBQUksQ0FBQztvQkFDL0IsS0FBSSxDQUFDLElBQUksR0FBRyxDQUFDLENBQUMsTUFBTSxDQUFDLENBQUM7b0JBQ3RCLEtBQUksQ0FBQyxPQUFPLEdBQUcsQ0FBQyxDQUFDLE1BQU0sQ0FBQyxDQUFDLE9BQU8sQ0FBQztvQkFDakMsT0FBTyxFQUFFLENBQUM7Z0JBQ2QsQ0FBQyxDQUFDO3FCQUNELEtBQUssQ0FBQyxVQUFBLENBQUM7b0JBQ0osS0FBSSxDQUFDLGtCQUFrQixHQUFHLEtBQUssQ0FBQztvQkFDaEMsS0FBSSxDQUFDLGlCQUFpQixHQUFHLHlDQUF5QyxDQUFDO29CQUNuRSxPQUFPLENBQUMsQ0FBQyxDQUFDLENBQUM7Z0JBQ2YsQ0FBQyxDQUFDLENBQUM7WUFDUCxDQUFDLENBQUMsQ0FBQztRQUNQLENBQUM7UUFFRCxrQkFBSSxHQUFKO1lBQUEsaUJBcUJDO1lBcEJHLElBQUksQ0FBQyxVQUFVLEdBQUcsSUFBSSxDQUFDO1lBQ3ZCLElBQUksQ0FBQyxTQUFTLEdBQUcsRUFBRSxDQUFDO1lBQ3BCLElBQU0sSUFBSSxHQUFHO2dCQUNULE1BQU0sRUFBRSxJQUFJLENBQUMsSUFBSSxDQUFDLElBQUksQ0FBQztnQkFDdkIsU0FBUyxFQUFFLElBQUksQ0FBQyxTQUFTO2FBQzVCLENBQUM7WUFDRixJQUFJLENBQUMsVUFBVSxDQUFDLFNBQVMsQ0FBQyxJQUFJLEVBQUUsUUFBUSxDQUFDLENBQUMsSUFBSSxDQUFDLFVBQUEsQ0FBQztnQkFDNUMsS0FBSSxDQUFDLFVBQVUsR0FBRyxLQUFLLENBQUM7Z0JBQ3hCLEVBQUUsQ0FBQyxDQUFDLENBQUMsQ0FBQyxTQUFTLENBQUMsQ0FBQyxDQUFDLENBQUM7b0JBQ2YsSUFBTSxNQUFJLEdBQUcsQ0FBQyxDQUFDLE1BQU0sQ0FBQyxDQUFDO29CQUN2QixLQUFJLENBQUMsT0FBTyxDQUFDLE1BQU0sQ0FBQyxHQUFHLE1BQUksQ0FBQyxPQUFPLENBQUM7b0JBQ3BDLEVBQUUsQ0FBQyxDQUFDLE1BQUksQ0FBQyxTQUFTLENBQUMsQ0FBQSxDQUFDO3dCQUNoQixLQUFJLENBQUMsU0FBUyxHQUFHLFFBQVEsR0FBRyxNQUFJLENBQUMsU0FBUyxDQUFDO29CQUMvQyxDQUFDO29CQUFDLElBQUksQ0FBQyxDQUFDO3dCQUNKLEtBQUksQ0FBQyxTQUFTLEdBQUcsUUFBUSxDQUFDO29CQUM5QixDQUFDO2dCQUNMLENBQUM7Z0JBQUMsSUFBSSxDQUFDLENBQUM7b0JBQ0osS0FBSSxDQUFDLFNBQVMsR0FBRyxPQUFPLENBQUM7Z0JBQzdCLENBQUM7WUFDTCxDQUFDLENBQUMsQ0FBQTtRQUNOLENBQUM7UUFDTCxVQUFDO0lBQUQsQ0F4REEsQUF3REMsSUFBQTtJQXhEWSxHQUFHO1FBRGYsMEJBQU0sQ0FBQywwQ0FBZSxFQUFFLHVCQUFVLENBQUM7O09BQ3ZCLEdBQUcsQ0F3RGY7SUF4RFksa0JBQUciLCJmaWxlIjoiYXBwLmpzIiwic291cmNlc0NvbnRlbnQiOlsiaW1wb3J0IHtpbmplY3R9IGZyb20gJ2F1cmVsaWEtZnJhbWV3b3JrJztcbmltcG9ydCB7RXZlbnRBZ2dyZWdhdG9yfSBmcm9tICdhdXJlbGlhLWV2ZW50LWFnZ3JlZ2F0b3InO1xuaW1wb3J0IHthcGlnYXRld2F5fSBmcm9tICcuL2Nvbm5lY3Rpb24vYXBpZ2F0ZXdheSc7XG5pbXBvcnQge3RyYWNlfSBmcm9tICcuL2NvbW1vbi9mdW5jdGlvbnMnO1xuXG5AaW5qZWN0KEV2ZW50QWdncmVnYXRvciwgYXBpZ2F0ZXdheSlcbmV4cG9ydCBjbGFzcyBBcHAge1xuICAgIGJldEFtb3VudDpTdHJpbmc7XG4gICAgd2luUmVzdWx0OlN0cmluZyA9IFwiXCI7XG4gICAgY29ubmVjdGlvbk1lc3NhZ2U6U3RyaW5nPVwiQ29ubmVjdGluZyB0byBzZXJ2ZXJcIjtcbiAgICBjb25uZWN0aW5nOmJvb2xlYW4gPSBmYWxzZTtcbiAgICBjb25uZWN0aW9uU3VjY2VkZWQ6Ym9vbGVhbiA9IGZhbHNlO1xuICAgIGFwaWdhdGV3YXk6YXBpZ2F0ZXdheTtcbiAgICBldmVudEFnZ3JlZ2F0b3I6RXZlbnRBZ2dyZWdhdG9yO1xuICAgIHVzZXI9e307XG4gICAgYmFsYW5jZT17fTtcblxuICAgIGNvbnN0cnVjdG9yKEV2ZW50QWdncmVnYXRvciwgYXBpZ2F0ZXdheSl7XG4gICAgICAgIHRoaXMuYmV0QW1vdW50ID0gXCIxMDAwXCI7XG4gICAgICAgIHRoaXMuY29ubmVjdGluZyA9IGZhbHNlO1xuICAgICAgICB0aGlzLmFwaWdhdGV3YXkgPSBhcGlnYXRld2F5O1xuICAgICAgICB0aGlzLmV2ZW50QWdncmVnYXRvciA9IEV2ZW50QWdncmVnYXRvcjtcbiAgICB9XG4gICAgYWN0aXZhdGUoKXtcbiAgICAgICAgcmV0dXJuIG5ldyBQcm9taXNlKChyZXNvbHZlLHJlamVjdCk9PnsgXG4gICAgICAgICAgICB0aGlzLmFwaWdhdGV3YXkuZG9BcGlDYWxsKHt9LCBcIi9nZXR1c2VyL1wiKVxuICAgICAgICAgICAgLnRoZW4obz0+e1xuICAgICAgICAgICAgICAgIHRoaXMuY29ubmVjdGlvblN1Y2NlZGVkID0gdHJ1ZTtcbiAgICAgICAgICAgICAgICB0aGlzLnVzZXIgPSBvW1wiZGF0YVwiXTtcbiAgICAgICAgICAgICAgICB0aGlzLmJhbGFuY2UgPSBvW1wiZGF0YVwiXS5iYWxhbmNlO1xuICAgICAgICAgICAgICAgIHJlc29sdmUoKTtcbiAgICAgICAgICAgIH0pXG4gICAgICAgICAgICAuY2F0Y2gobz0+e1xuICAgICAgICAgICAgICAgIHRoaXMuY29ubmVjdGlvblN1Y2NlZGVkID0gZmFsc2U7XG4gICAgICAgICAgICAgICAgdGhpcy5jb25uZWN0aW9uTWVzc2FnZSA9IFwiQ2FuJ3QgY29ubmVjdCB0byBzZXJ2ZXIsIGlzIGl0IHJ1bm5pbmc/XCI7XG4gICAgICAgICAgICAgICAgcmVzb2x2ZShvKTtcbiAgICAgICAgICAgIH0pO1xuICAgICAgICB9KTtcbiAgICB9XG5cbiAgICBwbGF5KCl7XG4gICAgICAgIHRoaXMuY29ubmVjdGluZyA9IHRydWU7XG4gICAgICAgIHRoaXMud2luUmVzdWx0ID0gXCJcIjtcbiAgICAgICAgY29uc3QgZGF0YSA9IHtcbiAgICAgICAgICAgIHVzZXJJZDogdGhpcy51c2VyW1wiaWRcIl0sXG4gICAgICAgICAgICBiZXRBbW91bnQ6IHRoaXMuYmV0QW1vdW50XG4gICAgICAgIH07XG4gICAgICAgIHRoaXMuYXBpZ2F0ZXdheS5kb0FwaUNhbGwoZGF0YSwgXCIvcGxheS9cIikudGhlbihvPT57XG4gICAgICAgICAgICB0aGlzLmNvbm5lY3RpbmcgPSBmYWxzZTtcbiAgICAgICAgICAgIGlmIChvW1wic3VjY2Vzc1wiXSkge1xuICAgICAgICAgICAgICAgIGNvbnN0IGRhdGEgPSBvW1wiZGF0YVwiXTtcbiAgICAgICAgICAgICAgICB0aGlzLmJhbGFuY2VbXCJnb2xkXCJdID0gZGF0YS5iYWxhbmNlO1xuICAgICAgICAgICAgICAgIGlmIChkYXRhLndpbkFtb3VudCl7XG4gICAgICAgICAgICAgICAgICAgIHRoaXMud2luUmVzdWx0ID0gXCJXaW4hICRcIiArIGRhdGEud2luQW1vdW50O1xuICAgICAgICAgICAgICAgIH0gZWxzZSB7XG4gICAgICAgICAgICAgICAgICAgIHRoaXMud2luUmVzdWx0ID0gXCJObyBXaW5cIjtcbiAgICAgICAgICAgICAgICB9XG4gICAgICAgICAgICB9IGVsc2Uge1xuICAgICAgICAgICAgICAgIHRoaXMud2luUmVzdWx0ID0gXCJFcnJvclwiO1xuICAgICAgICAgICAgfVxuICAgICAgICB9KVxuICAgIH1cbn1cbiJdLCJzb3VyY2VSb290Ijoic3JjIn0=

define('environment',["require", "exports"], function (require, exports) {
    "use strict";
    Object.defineProperty(exports, "__esModule", { value: true });
    exports.default = {
        debug: true,
        testing: true
    };
});

//# sourceMappingURL=data:application/json;charset=utf8;base64,eyJ2ZXJzaW9uIjozLCJzb3VyY2VzIjpbImVudmlyb25tZW50LnRzIl0sIm5hbWVzIjpbXSwibWFwcGluZ3MiOiI7OztJQUFBLGtCQUFlO1FBQ2IsS0FBSyxFQUFFLElBQUk7UUFDWCxPQUFPLEVBQUUsSUFBSTtLQUNkLENBQUMiLCJmaWxlIjoiZW52aXJvbm1lbnQuanMiLCJzb3VyY2VzQ29udGVudCI6WyJleHBvcnQgZGVmYXVsdCB7XG4gIGRlYnVnOiB0cnVlLFxuICB0ZXN0aW5nOiB0cnVlXG59O1xuIl0sInNvdXJjZVJvb3QiOiJzcmMifQ==

define('main',["require", "exports", "./environment"], function (require, exports, environment_1) {
    "use strict";
    Object.defineProperty(exports, "__esModule", { value: true });
    function configure(aurelia) {
        var thedomain = location.hostname;
        aurelia.use.instance("apiRoot", "http://" + thedomain + ":8090");
        aurelia.use
            .standardConfiguration()
            .feature('resources');
        if (environment_1.default.debug) {
            aurelia.use.developmentLogging();
        }
        if (environment_1.default.testing) {
            aurelia.use.plugin('aurelia-testing');
        }
        aurelia.start().then(function () { return aurelia.setRoot(); });
    }
    exports.configure = configure;
});

//# sourceMappingURL=data:application/json;charset=utf8;base64,eyJ2ZXJzaW9uIjozLCJzb3VyY2VzIjpbIm1haW4udHMiXSwibmFtZXMiOltdLCJtYXBwaW5ncyI6Ijs7O0lBR0EsbUJBQTBCLE9BQWdCO1FBQ3hDLElBQU0sU0FBUyxHQUFHLFFBQVEsQ0FBQyxRQUFRLENBQUM7UUFDcEMsT0FBTyxDQUFDLEdBQUcsQ0FBQyxRQUFRLENBQUMsU0FBUyxFQUFFLFlBQVUsU0FBUyxVQUFPLENBQUMsQ0FBQztRQUU1RCxPQUFPLENBQUMsR0FBRzthQUNSLHFCQUFxQixFQUFFO2FBQ3ZCLE9BQU8sQ0FBQyxXQUFXLENBQUMsQ0FBQztRQUV4QixFQUFFLENBQUMsQ0FBQyxxQkFBVyxDQUFDLEtBQUssQ0FBQyxDQUFDLENBQUM7WUFDdEIsT0FBTyxDQUFDLEdBQUcsQ0FBQyxrQkFBa0IsRUFBRSxDQUFDO1FBQ25DLENBQUM7UUFFRCxFQUFFLENBQUMsQ0FBQyxxQkFBVyxDQUFDLE9BQU8sQ0FBQyxDQUFDLENBQUM7WUFDeEIsT0FBTyxDQUFDLEdBQUcsQ0FBQyxNQUFNLENBQUMsaUJBQWlCLENBQUMsQ0FBQztRQUN4QyxDQUFDO1FBRUQsT0FBTyxDQUFDLEtBQUssRUFBRSxDQUFDLElBQUksQ0FBQyxjQUFNLE9BQUEsT0FBTyxDQUFDLE9BQU8sRUFBRSxFQUFqQixDQUFpQixDQUFDLENBQUM7SUFDaEQsQ0FBQztJQWpCRCw4QkFpQkMiLCJmaWxlIjoibWFpbi5qcyIsInNvdXJjZXNDb250ZW50IjpbImltcG9ydCB7QXVyZWxpYX0gZnJvbSAnYXVyZWxpYS1mcmFtZXdvcmsnXG5pbXBvcnQgZW52aXJvbm1lbnQgZnJvbSAnLi9lbnZpcm9ubWVudCc7XG5cbmV4cG9ydCBmdW5jdGlvbiBjb25maWd1cmUoYXVyZWxpYTogQXVyZWxpYSkge1xuICBjb25zdCB0aGVkb21haW4gPSBsb2NhdGlvbi5ob3N0bmFtZTtcbiAgYXVyZWxpYS51c2UuaW5zdGFuY2UoXCJhcGlSb290XCIsIGBodHRwOi8vJHt0aGVkb21haW59OjgwOTBgKTtcblxuICBhdXJlbGlhLnVzZVxuICAgIC5zdGFuZGFyZENvbmZpZ3VyYXRpb24oKVxuICAgIC5mZWF0dXJlKCdyZXNvdXJjZXMnKTtcblxuICBpZiAoZW52aXJvbm1lbnQuZGVidWcpIHtcbiAgICBhdXJlbGlhLnVzZS5kZXZlbG9wbWVudExvZ2dpbmcoKTtcbiAgfVxuXG4gIGlmIChlbnZpcm9ubWVudC50ZXN0aW5nKSB7XG4gICAgYXVyZWxpYS51c2UucGx1Z2luKCdhdXJlbGlhLXRlc3RpbmcnKTtcbiAgfVxuXG4gIGF1cmVsaWEuc3RhcnQoKS50aGVuKCgpID0+IGF1cmVsaWEuc2V0Um9vdCgpKTtcbn1cbiJdLCJzb3VyY2VSb290Ijoic3JjIn0=

var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};
define('connection/apigateway',["require", "exports", "aurelia-framework", "aurelia-fetch-client", "aurelia-event-aggregator", "./../common/functions"], function (require, exports, aurelia_framework_1, aurelia_fetch_client_1, aurelia_event_aggregator_1, functions_1) {
    "use strict";
    Object.defineProperty(exports, "__esModule", { value: true });
    var apigateway = (function () {
        function apigateway(apiRoot, EventAggregator, httpFetch) {
            this.apiRoot = apiRoot;
            this.eventAggregator = EventAggregator;
            httpFetch.configure(function (config) {
                config
                    .withDefaults({
                    credentials: 'include',
                    headers: {
                        'Accept': 'application/json',
                        'X-Requested-With': 'Fetch'
                    }
                })
                    .withInterceptor({
                    request: function (request) {
                        functions_1.trace("Requesting " + request.method + " " + request.url);
                        return request;
                    },
                });
            });
            this.httpFetch = httpFetch;
        }
        apigateway.prototype.doApiCall = function (myPostData, endPoint, isFormData) {
            var _this = this;
            if (myPostData === void 0) { myPostData = {}; }
            if (endPoint === void 0) { endPoint = ""; }
            if (isFormData === void 0) { isFormData = false; }
            var body = null;
            myPostData = Object.assign({}, myPostData);
            if (!isFormData) {
                body = JSON.stringify(myPostData);
            }
            else {
                body = myPostData;
            }
            return new Promise(function (resolve, reject) {
                _this.httpFetch.fetch(_this.apiRoot + endPoint, {
                    method: "POST",
                    body: body
                })
                    .then(function (response) { return response.json(); })
                    .then(function (o) { return resolve(o); })
                    .catch(function (o) { return reject(o); });
            });
        };
        return apigateway;
    }());
    apigateway = __decorate([
        aurelia_framework_1.inject('apiRoot', aurelia_event_aggregator_1.EventAggregator, aurelia_fetch_client_1.HttpClient),
        __metadata("design:paramtypes", [Object, Object, Object])
    ], apigateway);
    exports.apigateway = apigateway;
});

//# sourceMappingURL=data:application/json;charset=utf8;base64,eyJ2ZXJzaW9uIjozLCJzb3VyY2VzIjpbImNvbm5lY3Rpb24vYXBpZ2F0ZXdheS50cyJdLCJuYW1lcyI6W10sIm1hcHBpbmdzIjoiOzs7Ozs7Ozs7Ozs7SUFNQSxJQUFhLFVBQVU7UUFNbkIsb0JBQVksT0FBTyxFQUFDLGVBQWUsRUFBQyxTQUFTO1lBQ3pDLElBQUksQ0FBQyxPQUFPLEdBQUcsT0FBTyxDQUFDO1lBQ3ZCLElBQUksQ0FBQyxlQUFlLEdBQUcsZUFBZSxDQUFDO1lBRXZDLFNBQVMsQ0FBQyxTQUFTLENBQUMsVUFBQSxNQUFNO2dCQUN0QixNQUFNO3FCQUNELFlBQVksQ0FBQztvQkFDVixXQUFXLEVBQUUsU0FBUztvQkFDdEIsT0FBTyxFQUFFO3dCQUNMLFFBQVEsRUFBRSxrQkFBa0I7d0JBQzVCLGtCQUFrQixFQUFFLE9BQU87cUJBQzlCO2lCQUNKLENBQUM7cUJBQ0QsZUFBZSxDQUFDO29CQUNiLE9BQU8sWUFBQyxPQUFPO3dCQUNYLGlCQUFLLENBQUMsZ0JBQWMsT0FBTyxDQUFDLE1BQU0sU0FBSSxPQUFPLENBQUMsR0FBSyxDQUFDLENBQUM7d0JBQ3JELE1BQU0sQ0FBQyxPQUFPLENBQUM7b0JBQ25CLENBQUM7aUJBS0osQ0FBQyxDQUFDO1lBQ1gsQ0FBQyxDQUFDLENBQUM7WUFDSCxJQUFJLENBQUMsU0FBUyxHQUFHLFNBQVMsQ0FBQztRQUUvQixDQUFDO1FBRUQsOEJBQVMsR0FBVCxVQUFVLFVBQWEsRUFBRSxRQUFXLEVBQUUsVUFBZ0I7WUFBdEQsaUJBY0M7WUFkUywyQkFBQSxFQUFBLGVBQWE7WUFBRSx5QkFBQSxFQUFBLGFBQVc7WUFBRSwyQkFBQSxFQUFBLGtCQUFnQjtZQUNsRCxJQUFJLElBQUksR0FBRyxJQUFJLENBQUM7WUFDaEIsVUFBVSxHQUFHLE1BQU0sQ0FBQyxNQUFNLENBQUMsRUFBRSxFQUFFLFVBQVUsQ0FBQyxDQUFDO1lBQzNDLEVBQUUsQ0FBQyxDQUFDLENBQUMsVUFBVSxDQUFDLENBQUMsQ0FBQztnQkFBQyxJQUFJLEdBQUcsSUFBSSxDQUFDLFNBQVMsQ0FBQyxVQUFVLENBQUMsQ0FBQTtZQUFDLENBQUM7WUFBQyxJQUFJLENBQUMsQ0FBQztnQkFBQyxJQUFJLEdBQUcsVUFBVSxDQUFDO1lBQUMsQ0FBQztZQUVsRixNQUFNLENBQUMsSUFBSSxPQUFPLENBQUUsVUFBQyxPQUFPLEVBQUUsTUFBTTtnQkFDaEMsS0FBSSxDQUFDLFNBQVMsQ0FBQyxLQUFLLENBQUMsS0FBSSxDQUFDLE9BQU8sR0FBRyxRQUFRLEVBQUU7b0JBQzFDLE1BQU0sRUFBRSxNQUFNO29CQUNkLElBQUksRUFBRSxJQUFJO2lCQUNiLENBQUM7cUJBQ0QsSUFBSSxDQUFDLFVBQUEsUUFBUSxJQUFJLE9BQUEsUUFBUSxDQUFDLElBQUksRUFBRSxFQUFmLENBQWUsQ0FBQztxQkFDakMsSUFBSSxDQUFHLFVBQUEsQ0FBQyxJQUFJLE9BQUEsT0FBTyxDQUFDLENBQUMsQ0FBQyxFQUFWLENBQVUsQ0FBQztxQkFDdkIsS0FBSyxDQUFFLFVBQUEsQ0FBQyxJQUFJLE9BQUEsTUFBTSxDQUFDLENBQUMsQ0FBQyxFQUFULENBQVMsQ0FBQyxDQUFDO1lBQzVCLENBQUMsQ0FBQyxDQUFDO1FBQ1AsQ0FBQztRQUNMLGlCQUFDO0lBQUQsQ0FqREEsQUFpREMsSUFBQTtJQWpEWSxVQUFVO1FBRHRCLDBCQUFNLENBQUMsU0FBUyxFQUFFLDBDQUFlLEVBQUMsaUNBQVMsQ0FBQzs7T0FDaEMsVUFBVSxDQWlEdEI7SUFqRFksZ0NBQVUiLCJmaWxlIjoiY29ubmVjdGlvbi9hcGlnYXRld2F5LmpzIiwic291cmNlc0NvbnRlbnQiOlsiaW1wb3J0IHsgaW5qZWN0IH0gZnJvbSAnYXVyZWxpYS1mcmFtZXdvcmsnO1xuaW1wb3J0IHtIdHRwQ2xpZW50IGFzIGh0dHBGZXRjaCwganNvbn0gZnJvbSAnYXVyZWxpYS1mZXRjaC1jbGllbnQnO1xuaW1wb3J0IHtFdmVudEFnZ3JlZ2F0b3J9IGZyb20gJ2F1cmVsaWEtZXZlbnQtYWdncmVnYXRvcic7XG5pbXBvcnQge3RyYWNlfSBmcm9tICcuLy4uL2NvbW1vbi9mdW5jdGlvbnMnO1xuXG5AaW5qZWN0KCdhcGlSb290JywgRXZlbnRBZ2dyZWdhdG9yLGh0dHBGZXRjaClcbmV4cG9ydCBjbGFzcyBhcGlnYXRld2F5IHtcbiAgICBhcGlSb290OnN0cmluZztcbiAgICBldmVudEFnZ3JlZ2F0b3I7XG4gICAgaHR0cEZldGNoO1xuXG5cbiAgICBjb25zdHJ1Y3RvcihhcGlSb290LEV2ZW50QWdncmVnYXRvcixodHRwRmV0Y2gpe1xuICAgICAgICB0aGlzLmFwaVJvb3QgPSBhcGlSb290O1xuICAgICAgICB0aGlzLmV2ZW50QWdncmVnYXRvciA9IEV2ZW50QWdncmVnYXRvcjsgXG5cbiAgICAgICAgaHR0cEZldGNoLmNvbmZpZ3VyZShjb25maWcgPT4ge1xuICAgICAgICAgICAgY29uZmlnXG4gICAgICAgICAgICAgICAgLndpdGhEZWZhdWx0cyh7XG4gICAgICAgICAgICAgICAgICAgIGNyZWRlbnRpYWxzOiAnaW5jbHVkZScsIC8vIFZhbGlkIHZhbHVlczsgb21pdCwgc2FtZS1vcmlnaW4gYW5kIGluY2x1ZGVcbiAgICAgICAgICAgICAgICAgICAgaGVhZGVyczoge1xuICAgICAgICAgICAgICAgICAgICAgICAgJ0FjY2VwdCc6ICdhcHBsaWNhdGlvbi9qc29uJyxcbiAgICAgICAgICAgICAgICAgICAgICAgICdYLVJlcXVlc3RlZC1XaXRoJzogJ0ZldGNoJ1xuICAgICAgICAgICAgICAgICAgICB9XG4gICAgICAgICAgICAgICAgfSlcbiAgICAgICAgICAgICAgICAud2l0aEludGVyY2VwdG9yKHtcbiAgICAgICAgICAgICAgICAgICAgcmVxdWVzdChyZXF1ZXN0KSB7XG4gICAgICAgICAgICAgICAgICAgICAgICB0cmFjZShgUmVxdWVzdGluZyAke3JlcXVlc3QubWV0aG9kfSAke3JlcXVlc3QudXJsfWApOyBcbiAgICAgICAgICAgICAgICAgICAgICAgIHJldHVybiByZXF1ZXN0OyBcbiAgICAgICAgICAgICAgICAgICAgfSxcbiAgICAgICAgICAgICAgICAgICAgLy8gcmVzcG9uc2UocmVzcG9uc2UpIHtcbiAgICAgICAgICAgICAgICAgICAgLy8gICAgIGNvbnNvbGUubG9nKGBSZWNlaXZlZCAke3Jlc3BvbnNlLnN0YXR1c30gJHtyZXNwb25zZS51cmx9YCk7XG4gICAgICAgICAgICAgICAgICAgIC8vICAgICByZXR1cm4gcmVzcG9uc2U7IC8vIHlvdSBjYW4gcmV0dXJuIGEgbW9kaWZpZWQgUmVzcG9uc2VcbiAgICAgICAgICAgICAgICAgICAgLy8gfVxuICAgICAgICAgICAgICAgIH0pO1xuICAgICAgICB9KTtcbiAgICAgICAgdGhpcy5odHRwRmV0Y2ggPSBodHRwRmV0Y2g7XG5cbiAgICB9XG5cbiAgICBkb0FwaUNhbGwobXlQb3N0RGF0YT17fSwgZW5kUG9pbnQ9XCJcIiwgaXNGb3JtRGF0YT1mYWxzZSkge1xuICAgICAgICBsZXQgYm9keSA9IG51bGw7XG4gICAgICAgIG15UG9zdERhdGEgPSBPYmplY3QuYXNzaWduKHt9LCBteVBvc3REYXRhKTtcbiAgICAgICAgaWYgKCFpc0Zvcm1EYXRhKSB7IGJvZHkgPSBKU09OLnN0cmluZ2lmeShteVBvc3REYXRhKSB9IGVsc2UgeyBib2R5ID0gbXlQb3N0RGF0YTsgfVxuXG4gICAgICAgIHJldHVybiBuZXcgUHJvbWlzZSggKHJlc29sdmUsIHJlamVjdCkgPT4geyBcbiAgICAgICAgICAgIHRoaXMuaHR0cEZldGNoLmZldGNoKHRoaXMuYXBpUm9vdCArIGVuZFBvaW50LCB7XG4gICAgICAgICAgICAgICAgbWV0aG9kOiBcIlBPU1RcIixcbiAgICAgICAgICAgICAgICBib2R5OiBib2R5XG4gICAgICAgICAgICB9KVxuICAgICAgICAgICAgLnRoZW4ocmVzcG9uc2UgPT4gcmVzcG9uc2UuanNvbigpKVxuICAgICAgICAgICAgLnRoZW4oICBvID0+IHJlc29sdmUobykpXG4gICAgICAgICAgICAuY2F0Y2goIG8gPT4gcmVqZWN0KG8pKTsgXG4gICAgICAgIH0pO1xuICAgIH1cbn1cbiJdLCJzb3VyY2VSb290Ijoic3JjIn0=

define('resources/index',["require", "exports"], function (require, exports) {
    "use strict";
    Object.defineProperty(exports, "__esModule", { value: true });
    function configure(config) {
    }
    exports.configure = configure;
});

//# sourceMappingURL=data:application/json;charset=utf8;base64,eyJ2ZXJzaW9uIjozLCJzb3VyY2VzIjpbInJlc291cmNlcy9pbmRleC50cyJdLCJuYW1lcyI6W10sIm1hcHBpbmdzIjoiOzs7SUFFQSxtQkFBMEIsTUFBOEI7SUFFeEQsQ0FBQztJQUZELDhCQUVDIiwiZmlsZSI6InJlc291cmNlcy9pbmRleC5qcyIsInNvdXJjZXNDb250ZW50IjpbImltcG9ydCB7RnJhbWV3b3JrQ29uZmlndXJhdGlvbn0gZnJvbSAnYXVyZWxpYS1mcmFtZXdvcmsnO1xuXG5leHBvcnQgZnVuY3Rpb24gY29uZmlndXJlKGNvbmZpZzogRnJhbWV3b3JrQ29uZmlndXJhdGlvbikge1xuICAvL2NvbmZpZy5nbG9iYWxSZXNvdXJjZXMoW10pO1xufVxuIl0sInNvdXJjZVJvb3QiOiJzcmMifQ==

define('common/functions',["require", "exports"], function (require, exports) {
    "use strict";
    Object.defineProperty(exports, "__esModule", { value: true });
    function trace(o) {
        if (typeof console === "undefined") {
        }
        else {
            if (typeof o === "string") {
                console.log("%c" + o, 'background: #f8eafc; color: #302207');
            }
            else {
                console.log(o);
            }
        }
    }
    exports.trace = trace;
});

//# sourceMappingURL=data:application/json;charset=utf8;base64,eyJ2ZXJzaW9uIjozLCJzb3VyY2VzIjpbImNvbW1vbi9mdW5jdGlvbnMudHMiXSwibmFtZXMiOltdLCJtYXBwaW5ncyI6Ijs7O0lBQUEsZUFBc0IsQ0FBQztRQUNuQixFQUFFLENBQUMsQ0FBQyxPQUFPLE9BQU8sS0FBSyxXQUFXLENBQUMsQ0FBQyxDQUFDO1FBRXJDLENBQUM7UUFBQyxJQUFJLENBQUMsQ0FBQztZQUNKLEVBQUUsQ0FBQyxDQUFDLE9BQU8sQ0FBQyxLQUFLLFFBQVEsQ0FBQyxDQUFDLENBQUM7Z0JBQ3hCLE9BQU8sQ0FBQyxHQUFHLENBQUMsSUFBSSxHQUFDLENBQUMsRUFBRSxxQ0FBcUMsQ0FBQyxDQUFDO1lBQy9ELENBQUM7WUFBQyxJQUFJLENBQUMsQ0FBQztnQkFDSixPQUFPLENBQUMsR0FBRyxDQUFDLENBQUMsQ0FBQyxDQUFDO1lBQ25CLENBQUM7UUFDTCxDQUFDO0lBQ0wsQ0FBQztJQVZELHNCQVVDIiwiZmlsZSI6ImNvbW1vbi9mdW5jdGlvbnMuanMiLCJzb3VyY2VzQ29udGVudCI6WyJleHBvcnQgZnVuY3Rpb24gdHJhY2Uobykge1xuICAgIGlmICh0eXBlb2YgY29uc29sZSA9PT0gXCJ1bmRlZmluZWRcIikge1xuICAgICAgICAvL2NvbnNvbGUgbm90IGF2YWlsYWJsZVxuICAgIH0gZWxzZSB7XG4gICAgICAgIGlmICh0eXBlb2YgbyA9PT0gXCJzdHJpbmdcIikge1xuICAgICAgICAgICAgY29uc29sZS5sb2coXCIlY1wiK28sICdiYWNrZ3JvdW5kOiAjZjhlYWZjOyBjb2xvcjogIzMwMjIwNycpO1xuICAgICAgICB9IGVsc2Uge1xuICAgICAgICAgICAgY29uc29sZS5sb2cobyk7XG4gICAgICAgIH1cbiAgICB9XG59Il0sInNvdXJjZVJvb3QiOiJzcmMifQ==

define('text!app.html', ['module'], function(module) { module.exports = "<template><style type=\"text/css\">body,html{font-family:sans-serif}.info{font-size:12px}.balance{font-size:16px}</style><div show.bind=\"!connectionSucceded\">${connectionMessage}</div><div show.bind=\"connectionSucceded\"><h4>Roll the dice:</h4><div class=\"info\">User Id: ${user.id}</div><div class=\"info\">Username: ${user.name}</div><div class=\"info\">Email: ${user.email}</div><br><br><div class=\"balance\">Balance: ${balance.gold}</div><div class=\"info\">Bet Amount: <input type=\"text\" value.bind=\"betAmount\" disabled.bind=\"connecting\"></div><div><button type=\"text\" click.trigger=\"play()\" disabled.bind=\"connecting\">Roll Dice</button></div><h1 show.bind=\"winResult != ''\">${winResult}</h1></div></template>"; });
//# sourceMappingURL=app-bundle.js.map