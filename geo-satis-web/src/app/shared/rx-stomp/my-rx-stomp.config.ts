import { RxStompConfig } from '@stomp/rx-stomp';
import * as SockJS from 'sockjs-client';
import { environment } from 'src/environments/environment';

export const myRxStompConfig: RxStompConfig = {
  webSocketFactory: function () {
    return new SockJS(environment.webSocketUrl);
  },

  heartbeatIncoming: 0, // Typical value 0 - disabled
  heartbeatOutgoing: 20000, // Typical value 20000 - every 20 seconds

  reconnectDelay: 200,

  // debug: (msg: string): void => {
  //   console.log(new Date(), msg);
  // },
};
