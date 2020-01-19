import React, { Component,useState} from 'react';
import { Link, withRouter } from 'react-router-dom';
import { Button, Container, Form, FormGroup, Input, Label } from 'reactstrap';
import AppNavbar from './AppNavbar';
import { FormErrors } from './FormErrors';
import './Error.css';
import './CinemaEdit.css';
import { API_BASE_URL, ACCESS_TOKEN } from './utils/AuthConfig';
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
            formValid: false,
            auth: {}
        };
        this.handleChange = this.handleChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
        this.addTo = this.addTo.bind(this);
        this.deleteAllSeances = this.deleteAllSeances.bind(this);
    }
    
    async componentDidMount() {
        let id = this.props.match.params.id;
        console.log(id);
        let fromSeance = this.props.location.state;
        if(fromSeance !== null && fromSeance !== undefined){
           
            let ns = fromSeance.ns;
            console.log(ns);
            this.setState({
                name: ns.name,
                country: ns.country,
                region: ns.region,
                city: ns.city,
                street: ns.street,
                seances: ns.seances,
                formErrors: {name: '', country: '', region: '', city:'', street: ''},
                nameValid: true,
                countryValid: true,
                regionValid: true,
                cityValid: true,
                streetValid: true,
                seanceValid: true,
                formValid: true,
                auth: {}
            }, () => {for(let p in this.state){ this.validateField(p,this.state[p]);} });
        }
        else if (id !== 'new') {
            const cinema = await (await fetch('/v1/api/cinemas/'+id).then(response => response.json()));
            this.setState({
                name: cinema.name,
                country: cinema.country,
                region: cinema.region,
                city: cinema.city,
                street: cinema.street,
                seances: [],
                formErrors: {name: '', country: '', region: '', city:'', street: ''},
                nameValid: true,
                countryValid: true,
                regionValid: true,
                cityValid: true,
                streetValid: true,
                seanceValid: true,
                formValid: true,
                auth:{}
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
            nameValid = value.match(/^[A-Za-z][A-Za-z0-9\s]+$/);
            fieldValidationErrors.name = nameValid ? '' : ' is invalid. Require words!';
            nameValid = nameValid && value.length > 3;
            fieldValidationErrors.name = nameValid ? '' : fieldValidationErrors.name+' Length must be more than 3 characters!';
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
            regionValid = (value === null || value === '') || value.match(/^[A-Za-z_\s]+$/);
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
          console.log('Field = ',this.state.nameValid);
          this.setState({formValid: this.state.nameValid && this.state.countryValid && this.state.cityValid && this.state.regionValid && this.state.streetValid && this.state.seanceValid}, () => this.moveToP());
          console.log(this.state.formValid);
          //this.moveToP();
      }
    
     /*
     const [seances, setSeance] = React.useState(
     {seances: this.state.seances});*/
     addTo(s){
         //setSeance(
            let seances = this.state.seances;
            seances.push(s);
            this.setState({seances: seances});
         //);
     }
    
      moveToP(){
          
          let p = document.getElementsByClassName('has-error');
          console.log(p.length);
          if(p.length !== 0){
              Array.prototype.forEach.call(p, function(el,idx,arr){
                  //console.log(el.tagName);
                  if(el.tagName === 'P'){
                      let parid = el.textContent.split(' ')[0];
                      console.log('Id = ',parid);
                      let par = document.getElementById(parid).parentNode;
                      par.appendChild(el);
                  }
              },this);
          }
          return 1;
      }
    
    handleChange(event) {
        const target = event.target;
        const value = target.value;
        const name = target.name;
        this.setState({[name]: value},
                      () => { this.validateField(name, value) });
    }
    
    async handleSubmit(event){
        event.preventDefault();
        
         for(let p in this.state){
            this.validateField(p,this.state[p]);
        }
        if(!this.state.formValid){
            alert('Validation failed!');
            return -1;
        }
        
        const item = {
            name:this.state.name,
            country:this.state.country,
            city:this.state.city,
            region:this.state.region === "" ? null : this.state.region,
            street:this.state.street,
            seances:this.state.seances
        };
        await fetch('/v1/api/cinemas/'+this.props.match.params.id, {
            method: 'PATCH',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json',
                'Authorization':'Bearer '+localStorage.getItem(ACCESS_TOKEN)
            },
        body: JSON.stringify(item),
        }).then(response => response.json()).then(data => this.setState({auth: data}));
        if(this.state.auth !== undefined && this.state.auth.status !== undefined && this.state.auth.status === 401){
            //redirect if not authorized!
            var ns = {
                path: "/v1/api/cinemas"+this.props.match.params.id,
                req: {
                    method: 'PATCH',
                    headers: {
                        'Accept': 'application/json',
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(item)
                },
                rpath: '/v1/views/cinemas'
            };
            this.props.history.push('/views/login',{ns: ns});
            return;
        };
    this.props.history.push('/v1/views/cinemas');
    }
    
    errorClass(error) {
        return(error.length === 0 ? '' : ' has-error');
    }
    
    deleteAllSeances(item){
        this.setState({
                name: item.name,
                country: item.country,
                region: item.region,
                city: item.city,
                street: item.street,
                seances: [],
                formErrors: {name: '', country: '', region: '', city:'', street: ''},
                nameValid: true,
                countryValid: true,
                regionValid: true,
                cityValid: true,
                streetValid: true,
                seanceValid: true,
                formValid: true
        });
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
          <FormGroup className={'form-group'+this.errorClass(item.formErrors.name)}>
            <Label htmlFor="name">Name</Label>
            <Input type="text" name="name" id="name" className="form-control" value={item.name || ''}
                   onChange={this.handleChange} autoComplete="name"/>
          </FormGroup>
          <FormGroup className={'form-group'+this.errorClass(item.formErrors.country)}>
            <Label htmlFor="country">Country</Label>
            <Input type="text" name="country" id="country" className="form-control" value={item.country || ''}
                   onChange={this.handleChange} autoComplete="country"/>
          </FormGroup>
          <FormGroup className={'form-group'+this.errorClass(item.formErrors.city)}>
            <Label htmlFor="city">City</Label>
            <Input type="text" name="city" id="city" className="form-control" value={item.city || ''}
                   onChange={this.handleChange} autoComplete="city"/>
          </FormGroup>
          <FormGroup className={'form-group'+this.errorClass(item.formErrors.region)}>
            <Label htmlFor="region">Region</Label>
            <Input type="text" name="region" id="region" className="form-control" value={item.region || ''}
                   onChange={this.handleChange} autoComplete="region"/>
          </FormGroup>
          <FormGroup className={'form-group'+this.errorClass(item.formErrors.street)}>
            <Label htmlFor="street">Street</Label>
            <Input type="text" name="street" id="street" className="form-control" value={item.street || ''}
                   onChange={this.handleChange} autoComplete="street"/>
          </FormGroup>
          <FormGroup>
            <Button color="success" tag={Link} to={{pathname:"/views/cinemas/"+this.props.match.params.id+"/seances/add", state: {ns: item}}} className="btn btn-primary" >
                Add Seance
            </Button>
            {(item.seances.length > 0) ? <Button color="danger" className="btn btn-primary btn-scs-rm" onClick={() => this.deleteAllSeances(item)}>Clear all seances</Button> : null}
            <p>Count: {item.seances.length}</p>
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