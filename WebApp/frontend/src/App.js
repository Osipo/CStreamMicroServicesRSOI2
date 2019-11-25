import React, { Component } from 'react';
import logo from './logo.svg';
import './App.css';
import { BrowserRouter as Router, Route, Switch } from 'react-router-dom';
import Home from './Home';
import FilmList from './FilmList';
import GenreList from './GenreList';
import GenreEdit from './GenreEdit';
const ReactDOM = require('react-dom');

class App extends Component {
    
    render(){      
      return (
        <Router>
            <Switch>
                <Route path='/' exact={true} component={Home}/>
                <Route path='/v1/views/films' exact={true} component={FilmList} />
                <Route path='/v1/views/genres' exact={true} component={GenreList} />
                <Route path='/v1/genres/new' exact={true} component={GenreEdit}/>
            </Switch>
        </Router>
      );
    }
}

export default App;