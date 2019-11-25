import React, { Component } from 'react';
import { Button, ButtonGroup, Container, Table } from 'reactstrap';
import AppNavbar from './AppNavbar';
import { Link } from 'react-router-dom';

class SeanceCinemaList extends Component {

  constructor(props) {
    super(props);
    this.state = {seances: [], isLoading: true};
  }
  
  componentDidMount() {
    this.setState({isLoading: true});

    fetch('/v1/api/cinemas/2/seances')
      .then(response => response.json())
      .then(data => this.setState({cinemas: data, isLoading: false}));
  }
  
  render() {
    const {cinemas, isLoading} = this.state;

    if (isLoading) {
      return <p>Loading...</p>;
    }

    const cList = cinemas.map(c => {
      return <tr key={c.id}>
        <td style={{whiteSpace: 'nowrap'}}>{c.name}</td>
        <td>{c.country}</td>
        <td>{c.city}  {c.region} {c.street}</td>
        <td>
          <ButtonGroup>
            <Button size="sm" color="success" tag={Link} to={"/cinemas/"+c.id}>Edit</Button> {/*Edit cinema.*/}
            <Button size="sm" color="primary" tag={Link} to={"/views/seances"}>Seances</Button> {/*All Seances in cinema*/}
          </ButtonGroup>
        </td>
      </tr>
    });
    
    
    return (
      <div>
        <AppNavbar/>
        <Container fluid>
          <div className="float-right">
            <Button color="success" tag={Link} to="/cinemas/new">Add Cinema</Button>
          </div>
          <h3>Films</h3>
          <Table className="mt-4">
            <thead>
            <tr>
              <th width="20%">Name</th>
              <th width="10%">Country</th>
              <th width="20%">Address</th>
              <th width="10%">Actions</th>
            </tr>
            </thead>
            <tbody>
            {cList}
            </tbody>
          </Table>
        </Container>
      </div>
    );
  }
  
}
export default SeanceCinemaList;