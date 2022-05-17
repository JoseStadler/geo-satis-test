import { RxStompConfig } from '@stomp/rx-stomp';
import * as SockJS from 'sockjs-client';
import { environment } from 'src/environments/environment';

export const myRxStompConfig: RxStompConfig = {
  webSocketFactory: function () {
    return new SockJS(environment.webSocketUrl);
  },

  heartbeatIncoming: 0,
  heartbeatOutgoing: 20000,

  reconnectDelay: 200,
};
