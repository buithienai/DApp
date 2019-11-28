import React, { Component } from 'react';
import { BrowserRouter as Router, Route, IndexRoute, Switch } from 'react-router-dom';
import { Provider } from 'react-redux';

import MediaGalleryPage from '../containers/MediaGalleryPage';
import App from '../containers/App';
import HomePage from '../components/HomePage';

class Routes extends Component {


  render() {

    return (
      <Router>
        <Route path="/" component={HomePage} />
        {/* <Route component={HomePage} /> */}
        <Route path="/library" component={MediaGalleryPage} />
      </Router>
    );
  }

}

export default Routes;

// export default (
//   <Route >
//     <Route path="/" component={App} />
//     <Route path="/library" component={MediaGalleryPage} />
//   </Route>
// );
