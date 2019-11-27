import React, { Component } from 'react';
import { Button, ButtonGroup, Container, Table } from 'reactstrap';
import AppNavbar from './AppNavbar';
class SeanceInformation extends Component {
    constructor(props) {
        super(props);
    }
    render(){
        return (
            <div>
            <AppNavbar/>
            <Container fluid>
                <strong>Information is preparing...</strong>
            </Container>
            </div>
        );
    }
}
export default SeanceInformation;