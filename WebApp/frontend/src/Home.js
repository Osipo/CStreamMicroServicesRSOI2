import React, { Component } from 'react';
import './App.css';
import AppNavbar from './AppNavbar';
import { Link } from 'react-router-dom';
import { Button, Container } from 'reactstrap';
const REACT_VERSION = React.version;
class Home extends Component {
  render() {
    return (
      <div>
        <AppNavbar/>
        <Container fluid>
          <Button color="link"><Link to="/views/films">Manage Films</Link></Button>
          <Button color="link"><Link to="/views/genres">Manage Genres</Link></Button>
          <Button color="link"><Link to="/views/cinemas">Manage Cinemas</Link></Button>
          <div>React version: {REACT_VERSION}</div>
        </Container>
      </div>
    );
  }
}

export default Home;