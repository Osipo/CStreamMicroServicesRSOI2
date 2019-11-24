import React, { Component } from 'react';
import logo from './logo.svg';
import './App.css';
import { BrowserRouter as Router, Route, Switch } from 'react-router-dom';
import Home from './Home';
import FilmList from './FilmList';
import GenreList from './GenreList';
const ReactDOM = require('react-dom');

class App extends Component {
    
    render(){      
      return (
        <Router>
            <Switch>
                <Route path='/' exact={true} component={Home}/>
                <Route path='/api/films' exact={true} component={FilmList} />
                <Route path='/v1/api/genres' exact={true} component={GenreList} />
            </Switch>
        </Router>
      );
    }
}

export default App;