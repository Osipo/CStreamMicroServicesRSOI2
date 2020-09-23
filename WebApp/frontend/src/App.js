import React, { Component } from 'react';
import logo from './logo.svg';
import './App.css';
import { BrowserRouter as Router, Route, Switch } from 'react-router-dom';
import Home from './Home';
import GenList from './GenList';
import FilmList from './FilmList';
import FilmEdit from './FilmEdit';
import GenreList from './GenreList';
import GenreEdit from './GenreEdit';
import CinemaList from './CinemaList';
import CinemaEdit from './CinemaEdit';
import SeanceCinemaList from './SeanceCinemaList';
import SeanceInformation from './SeanceInformation';
import SeanceList from './SeanceList';
import SeanceEdit from './SeanceEdit';
import Login from './auth/Login';
import Signup from './auth/Signup';
const ReactDOM = require('react-dom');

class App extends Component {
    
    render(){      
      return (
        <Router>
            <Switch>
                <Route path='/' exact={true} component={Home}/>
                <Route path='/views/films' exact={true} render={(props) => (<GenList {...props} path={"/FilmService/v1/films/"} entity={"film"}/>)} />
                
                <Route path='/views/genres' exact={true} component={GenreList} />
                <Route path='/views/seances' exact={true} component={SeanceList}/>
                <Route path='/views/cinemas' exact={true} render={(props) => (<GenList {...props} path={"/CinemaService/v1/cinemas/"} entity={"cinema"}/>)}/>
                
                <Route path='/views/cinemas/:id' exact={true} component={CinemaEdit}/>
                
                <Route path='/views/genres/new' exact={true} component={GenreEdit}/>
                <Route path='/views/films/:id'  component={FilmEdit} />
                <Route path='/views/cinemas/:id/seances' exact={true} component={SeanceCinemaList} />
                <Route path='/views/seances/:cid/:fid' exact={true} component={SeanceInformation} />
                <Route path='/views/cinemas/:id/seances/add' exact={true} component={SeanceEdit} />
                <Route path='/views/login' exact={true} component={Login} />
                <Route path='/views/signup' exact={true} component={Signup} />
            </Switch>
        </Router>
      );
    }
}

export default App;