import React, { Component } from 'react';
import { Link, withRouter } from 'react-router-dom';
import { Button, Container, Form, FormGroup, Input, Label } from 'reactstrap';
import AppNavbar from './AppNavbar';
import { FormErrors } from './FormErrors';
import './Error.css';
class CinemaEdit extends Component {
    constructor(props){
        super(props);
        this.state = {
            name:'',
            country: '',
            region:'',
            city: '',
            street: '',
            seances: [],
            formErrors: {name: '', country: '', region: '', city: '', street: ''},
            nameValid: false,
            countryValid: false,
            regionValid: true,
            cityValid: false,
            streetValid: false,
            seanceValid: true,
            formValid: false
        };
        this.handleChange = this.handleChange.bind(this);
    }
    
    async componentDidMount() {
        let id = this.props.match.params.id;
        console.log(id);
        if (id !== 'new') {
            const cinema = await (await fetch('/v1/api/cinemas/'+id).then(response => response.json()));
            this.setState({
                name: cinema.name,
                country: cinema.country,
                region: cinema.region,
                city: cinema.city,
                street: cinema.street,
                formErrors: {name: '', rating: '', gname: ''},
                nameValid: true,
                countryValid: true,
                regionValid: true,
                cityValid: true,
                streetValid: true,
                seanceValid: true,
                formValid: true
            }, () => {for(let p in this.state){ this.validateField(p,this.state[p]);} });
        }
    }
    
     validateField(fieldName, value) {
        const item = this.state;
        let fieldValidationErrors = item.formErrors;
        let nameValid = item.nameValid;
        let countryValid = item.countryValid;
        let cityValid = item.cityValid;
        let regionValid = item.regionValid;
        let streetValid = item.streetValid;
        let seanceValid = item.seanceValid;
        switch(fieldName) {
          case 'name':
            nameValid = value.match(/^[A-Za-z\s]+$/);
            fieldValidationErrors.name = nameValid ? '' : ' is invalid. Require words!';
            nameValid = nameValid && value.length > 5;
            fieldValidationErrors.name = nameValid ? '' : fieldValidationErrors.name+' Length must be more than 5 characters!';
            break;
          case 'country':
            countryValid =  value.match(/^[A-Za-z\s]+$/);
            fieldValidationErrors.country = countryValid ? '': ' is invalid. Require words!';
            countryValid = countryValid && value.length > 2;
            fieldValidationErrors.country = countryValid ? '' : fieldValidationErrors.country+' Length must be more than 2 characters!';
            break;
          case 'city':
            cityValid = value.match(/^[A-Za-z\s]+$/);
            fieldValidationErrors.city = cityValid ? '' : ' is invalid. Require words!';
            cityValid = cityValid && value.length > 3;
            fieldValidationErrors.city = cityValid ? '' : fieldValidationErrors.city+' Length must be more than 3 characters!';
            break;
          case 'region':
            regionValid = value.match(/^[A-Za-z\s]+$/);
            fieldValidationErrors.region = regionValid ? '' : ' is invalid. Require words!';
            break;
          case 'street':
            streetValid = value.match(/^[A-Za-z\s]+$/);
            fieldValidationErrors.street = streetValid ? '' : ' is invalid. Require words!';
            streetValid = streetValid && value.length > 4;
            fieldValidationErrors.street = streetValid ? '' : fieldValidationErrors.street+' Length must be greater than 4 characters!';
            break;
          default:
            break;
        }
        this.setState({formErrors: fieldValidationErrors,
                        nameValid: nameValid,
                        countryValid: countryValid,
                        regionValid: regionValid,
                        cityValid: cityValid,
                        streetValid: streetValid,
                        seanceValid: true
                      }, () => this.validateForm());
      }

      validateForm() {
          this.setState({formValid: this.state.nameValid && this.state.countryValid && this.state.cityValid && this.state.regionValid && this.state.streetValid && this.state.seanceValid});
          console.log(this.state.formValid);
      }
    
    handleChange(event) {
        const target = event.target;
        const value = target.value;
        const name = target.name;
        this.setState({[name]: value},
                      () => { this.validateField(name, value) });
    }
    
    errorClass(error) {
        return(error.length === 0 ? '' : 'has-error');
    }
    
    render(){
        const item = this.state;
        const title = <h2>{item.name !== '' ? 'Edit Cinema' : 'Add Cinema'}</h2>;
        
        return <div>
      <AppNavbar meid={3}/>
      <Container>
        {title}
        <Form onSubmit={this.handleSubmit}>
          <div className="panel panel-default">
            <FormErrors formErrors={item.formErrors} />
          </div>
          <FormGroup className='{form-group ${this.errorClass(item.formErrors.name)}}'>
            <Label htmlFor="name">Name</Label>
            <Input type="text" name="name" id="name" className="form-control" value={item.name || ''}
                   onChange={this.handleChange} autoComplete="name"/>
          </FormGroup>
          <FormGroup className='{form-group ${this.errorClass(item.formErrors.rating)}}'>
            <Label htmlFor="country">Country</Label>
            <Input type="text" name="country" id="country" className="form-control" value={item.country || ''}
                   onChange={this.handleChange} autoComplete="country"/>
          </FormGroup>
          <FormGroup className='{form-group ${this.errorClass(item.formErrors.rating)}}'>
            <Label htmlFor="city">City</Label>
            <Input type="text" name="city" id="city" className="form-control" value={item.city || ''}
                   onChange={this.handleChange} autoComplete="city"/>
          </FormGroup>
          <FormGroup className='{form-group ${this.errorClass(item.formErrors.rating)}}'>
            <Label htmlFor="region">Region</Label>
            <Input type="text" name="region" id="region" className="form-control" value={item.region || ''}
                   onChange={this.handleChange} autoComplete="region"/>
          </FormGroup>
          <FormGroup className='{form-group ${this.errorClass(item.formErrors.rating)}}'>
            <Label htmlFor="street">Street</Label>
            <Input type="text" name="street" id="street" className="form-control" value={item.street || ''}
                   onChange={this.handleChange} autoComplete="street"/>
          </FormGroup>
          <FormGroup>
            <Button color="primary" type="submit" className="btn btn-primary" disabled={!item.formValid}>Save</Button>{' '}
            <Button color="secondary" tag={Link} to="/views/cinemas">Cancel</Button>
          </FormGroup>
        </Form>
      </Container>
    </div>
        
    }
}
export default withRouter(CinemaEdit);