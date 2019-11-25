import React, { Component } from 'react';
import { Button, ButtonGroup, Container, Table } from 'reactstrap';
import AppNavbar from './AppNavbar';
import { Link } from 'react-router-dom';

class GenreList extends Component {

  constructor(props) {
    super(props);
    this.state = {genres: [], isLoading: true};
    this.remove = this.remove.bind(this);
  }

  async remove(id) {
    await fetch('/v1/api/genres/delete/'+id, {
      method: 'POST',
      headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/json'
      }
    }).then(() => {
      let updated = [...this.state.genres].filter(i => i.id !== id);
      this.setState({genres: updated});
    });
  }
  
  componentDidMount() {
    this.setState({isLoading: true});

    fetch('/v1/api/genres')
      .then(response => response.json())
      .then(data => this.setState({genres: data, isLoading: false}));
  }
  
  render() {
    const {genres, isLoading} = this.state;

    if (isLoading) {
      return <p>Loading...</p>;
    }

    const gList = genres.map(g => {
      return <tr key={g.id}>
        <td style={{whiteSpace: 'nowrap'}}>{g.name}</td>
        <td>{g.remarks}</td>
        <td>
          <ButtonGroup>
            <Button size="sm" color="danger" tag={Link} to={"/views/genres"} onClick={() => this.remove(g.id)}>Delete</Button> {/*Delete genre.*/}
          </ButtonGroup>
        </td>
      </tr>
    });
    
    
    return (
      <div>
        <AppNavbar/>
        <Container fluid>
          <div className="float-right">
            <Button color="success" tag={Link} to="/genres/new">Add Genre</Button>
          </div>
          <h3>Genres</h3>
          <Table className="mt-4">
            <thead>
            <tr>
              <th width="20%">Name</th>
              <th width="20%">Remarks</th>
              <th width="10%">Actions</th>
            </tr>
            </thead>
            <tbody>
            {gList}
            </tbody>
          </Table>
        </Container>
      </div>
    );
  }
  
}
export default GenreList;