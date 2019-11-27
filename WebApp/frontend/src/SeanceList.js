import React, { Component } from 'react';
import { Button, ButtonGroup, Container, Table } from 'reactstrap';
import AppNavbar from './AppNavbar';
import { Link } from 'react-router-dom';

class SeanceList extends Component {

  constructor(props) {
    super(props);
    this.state = {seances: [], isLoading: true};
  }
  
  componentDidMount() {
    this.setState({isLoading: true});

    fetch('/v1/api/seances')
      .then(response => response.json())
      .then(data => this.setState({seances: data, isLoading: false}));
  }
  
  render() {
    const {seances, isLoading} = this.state;

    if (isLoading) {
      return <p>Loading...</p>;
    }

    const sList = seances.map(s => {
      return <tr key={s.cid+" "+s.fid}>
        <td>{s.cid}</td>
        <td>{s.fid}</td>
        <td style={{whiteSpace: 'nowrap'}}>{s.date}</td>
        <td>
          <ButtonGroup>
            <Button size="sm" color="primary" tag={Link} to={"/views/seances/"+s.cid+"/"+s.fid}>More info</Button> {/*Get more information about this seance*/}
          </ButtonGroup>
        </td>
      </tr>
    });
    
    
    return (
      <div>
        <AppNavbar meid={4}/>
        <Container fluid>
          <div className="float-right">
            <Button color="danger" tag={Link} to="/">Back</Button>
          </div>
          <h3>Seances</h3>
          <Table className="mt-4">
            <thead>
            <tr>
              <th width="15%">Cinema ID</th>
              <th width="15%">Film ID</th>
              <th width="25%">Date</th>
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
export default SeanceList;