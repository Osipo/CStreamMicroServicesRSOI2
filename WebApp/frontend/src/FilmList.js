import React, { Component } from 'react';
import { Button, ButtonGroup, Container, Table } from 'reactstrap';
import AppNavbar from './AppNavbar';
import { Link } from 'react-router-dom';

class FilmList extends Component {

  constructor(props) {
    super(props);
    this.state = {films: [], isLoading: true};
    //this.remove = this.remove.bind(this);
  }

  componentDidMount() {
    this.setState({isLoading: true});

    fetch('/v1/api/films')
      .then(response => response.json())
      .then(data => this.setState({films: data, isLoading: false}));
  }
  
   render() {
    const {films, isLoading} = this.state;

    if (isLoading) {
      return <p>Loading...</p>;
    }

    const fList = films.map(f => {
      const genre = f.genre;
      let g = '';
      if(genre){
          g = '${f.genre.name || ""}     ${f.genre.remarks || ""}'; 
      }
      return <tr key={f.id}>
        <td style={{whiteSpace: 'nowrap'}}>{f.name}</td>
        <td>{f.rating}</td>
        <td>
            <div key={f.genre.id}>{f.genre.name}</div>
        </td>
        <td>
          <ButtonGroup>
            <Button size="sm" color="primary" tag={Link} to={"/"}>Edit</Button> {/*Edit is not very yet.*/}
          </ButtonGroup>
        </td>
      </tr>
    });
    
    
    return (
      <div>
        <AppNavbar/>
        <Container fluid>
          <div className="float-right">
            <Button color="danger" tag={Link} to="/">Back</Button>
          </div>
          <h3>Films</h3>
          <Table className="mt-4">
            <thead>
            <tr>
              <th width="20%">Name</th>
              <th width="20%">Rating</th>
              <th>Genre</th>
              <th width="10%">Actions</th>
            </tr>
            </thead>
            <tbody>
            {fList}
            </tbody>
          </Table>
        </Container>
      </div>
    );
  }
}    
export default FilmList;