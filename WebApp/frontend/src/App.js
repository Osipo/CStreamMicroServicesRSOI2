import React, { Component } from 'react';
import logo from './logo.svg';
import './App.css';
import { BrowserRouter as Router, Route, Switch } from 'react-router-dom';
import Home from './Home';
import FilmList from './FilmList';
import FilmEdit from './FilmEdit';
import GenreList from './GenreList';
import GenreEdit from './GenreEdit';
import CinemaList from './CinemaList';
import SeanceCinemaList from './SeanceCinemaList';
const ReactDOM = require('react-dom');

class App extends Component {
    
    render(){      
      return (
        <Router>
            <Switch>
                <Route path='/' exact={true} component={Home}/>
                <Route path='/views/films' exact={true} component={FilmList} />
                <Route path='/views/genres' exact={true} component={GenreList} />
                <Route path='/views/cinemas' exact={true} component={CinemaList}/>
                <Route path='/genres/new' exact={true} component={GenreEdit}/>
                <Route path='/films/:id' exact={true} component={FilmEdit} />
                <Route path='/views/cinemas/seances/:id' exact={true} component={SeanceCinemaList} />
            </Switch>
        </Router>
      );
    }
}

export default App;