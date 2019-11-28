import React, { Component } from 'react';
import { Button, ButtonGroup, Container, Table } from 'reactstrap';
import AppNavbar from './AppNavbar';
class SeanceInformation extends Component {
    constructor(props) {
        super(props);
        this.state = {seance: null, isLoading: true};
    }
    
    componentDidMount() {
        this.setState({isLoading: true});

        fetch('/v1/api/cinemas/'+this.props.match.params.cid+'/seances/'+this.props.match.params.fid)
          .then(response => response.json())
          .then(data => this.setState({seance: data, isLoading: false}));
    }
    
    render(){
        const {seance, isLoading} = this.state;

        if (isLoading) {
          return <p>Loading...</p>;
        }
        
        return (
            <div>
            <AppNavbar/>
            <Container fluid>
                <h2>Cinema: {seance.cinema}</h2>
                <p>Location: {seance.location}</p>
                <h3>Film: {seance.film}</h3>
                <p>Genre: {seance.genre}</p>
                <p>Rating: {seance.rating}</p>
                <p>Date: {seance.date}</p>
            </Container>
            </div>
        );
    }
}
export default SeanceInformation;