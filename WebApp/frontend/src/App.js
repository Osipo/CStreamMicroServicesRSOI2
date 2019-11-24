import React, { Component } from 'react';
import logo from './logo.svg';
import './App.css';
const ReactDOM = require('react-dom');
class App extends Component {
    state = {
        isLoading: true,
        films: []
    };
    
    async componentDidMount() {
        const response = await fetch('/v1/api/films');
        const body = await response.json();
        this.setState({ films: body, isLoading: false });
    }

    
    render(){
        const {films, isLoading} = this.state;
        if(isLoading){
            return <p>Loading...</p>;
        }
      
      return (
        <div className="App">
          <header className="App-header">
            <img src={logo} className="App-logo" alt="logo" />
            /*
            <p>
              Edit <code>src/App.js</code> and save to reload.
            </p>
            <a
              className="App-link"
              href="https://reactjs.org"
              target="_blank"
              rel="noopener noreferrer"
            >
              Learn React
            </a>*/
            <div className="App-intro">
                <h2>Film list</h2>
                {films.map(f => <div key={film.id}>{film.name}</div>)}
            </div>
          </header>
        </div>
        );
        
        
    }
}

export default App;
/*
class FilmList extends React.Component{
	render() {
		const films = this.props.films.map(film =>
			<Film key={film._links.self.href} film={film}/>
		);
		return (
			<table>
				<tbody>
					<tr>
						<th>ID</th>
						<th>Name/th>
						<th>Rating</th>
						<th>Genre</th>
					</tr>
					{films}
				</tbody>
			</table>
		);
	}
}

class Film extends React.Component{
	render() {
		return (
			<tr>
				<td>{this.props.film.id}</td>
				<td>{this.props.film.name}</td>
				<td>{this.props.film.rating}</td>
				<td>{this.props.film.genre}</td>
			</tr>
		);
	}
}
class Genre extends React.Component {
    render(){
        return (
            {this.props.genre.name}
        );
    }
}*/