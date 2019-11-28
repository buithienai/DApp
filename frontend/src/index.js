import React from 'react';
import ReactDOM from 'react-dom';
import { BrowserRouter } from 'react-router-dom';
import { createBrowserHistory } from 'history';


import Routes from './routes/Routes';

import * as serviceWorker from './serviceWorker';

ReactDOM.render(
    <Routes />,
    document.getElementById('root')
);


