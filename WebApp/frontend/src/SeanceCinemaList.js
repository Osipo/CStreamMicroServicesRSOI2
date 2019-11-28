import React, { Component } from 'react';
import { Button, ButtonGroup, Container, Table } from 'reactstrap';
import AppNavbar from './AppNavbar';
import { Link } from 'react-router-dom';

class SeanceCinemaList extends Component {

  constructor(props) {
    super(props);
    this.state = {cs: [], isLoading: true};
  }
  
  componentDidMount() {
    this.setState({isLoading: true});
    let id = this.props.match.params.id;
    console.log(id);
    fetch('/v1/api/cinemas/'+id+'/seances')
      .then(response => response.json())
      .then(data => this.setState({cs: data, isLoading: false}));
  }
  
  render() {
    const {cs, isLoading} = this.state;

    if (isLoading) {
      return <p>Loading...</p>;
    }
    let s = cs.seances;
    let ci = cs.cinema;
    const sList = s.map(s => {
      return <tr key={s.cid+" "+s.fid}>
        <td style={{whiteSpace: 'nowrap'}}>{ci.name}</td>
        <td>{s.fid}</td>
        <td>{s.date}</td>
        <td>
          <ButtonGroup>
            <Button size="sm" color="primary" tag={Link} to={"/views/seances/"+s.cid+"/"+s.fid}>Info</Button> {/*Get more info about seance*/}
          </ButtonGroup>
        </td>
      </tr>
    });
    
    
    return (
      <div>
        <AppNavbar meid={3}/>
        <Container fluid>
          <div className="float-right">
            <Button color="danger" tag={Link} to="/views/cinemas">Back</Button>
          </div>
          <h3>Seances in {ci.Name}</h3>
          <Table className="mt-4">
            <thead>
            <tr>
              <th width="20%">Cinema</th>
              <th width="10%">FilmId</th>
              <th width="20%">Date</th>
              <th width="10%">Actions</th>
            </tr>
            </thead>
            <tbody>
            {sList}
            </tbody>
          </Table>
        </Container>
      </div>
    );
  }
  
}
export default SeanceCinemaList;