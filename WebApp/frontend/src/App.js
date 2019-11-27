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
import SeanceInformation from './SeanceInformation';
import SeanceList from './SeanceList';
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
                <Route path='/views/seances' exact={true} component={SeanceList}/>
                <Route path='/views/genres/new' exact={true} component={GenreEdit}/>
                <Route path='/views/films/:id'  component={FilmEdit} />
                <Route path='/views/cinemas/:id/seances' exact={true} component={SeanceCinemaList} />
                <Route path='/views/seances/:cid/:fid' exact={true} component={SeanceInformation} />
            </Switch>
        </Router>
      );
    }
}

export default App;