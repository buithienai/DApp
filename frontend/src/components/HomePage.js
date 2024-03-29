import React from 'react';

import { Route, Link } from 'react-router-dom';
import App from '../containers/App';

const HomePage = () => (
  <App>
    <div className="jumbotron center">
      <h1 className="lead">Welcome to Media Library built with React, Redux, and Redux-saga </h1>
      <div>
        <Link to="library">
          <button className="btn btn-lg btn-primary"> Visit Library</button>
        </Link>
      </div>
    </div>
  </App>
);
export default HomePage;