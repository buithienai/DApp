import React from 'react';
import { Route, Link } from 'react-router-dom';

const Header = () => (
  <div className="text-center">
    <nav className="navbar navbar-default">
      <Link to="/" className="active">Home</Link>
      {" | "}
      <Link to="library" className="active">Library</Link>
    </nav>
  </div>
);
export default Header;