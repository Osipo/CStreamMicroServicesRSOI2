import React, { Component } from 'react';
import { Link, withRouter } from 'react-router-dom';
import { Button, Container, Form, FormGroup, Input, Label } from 'reactstrap';
import AppNavbar from './AppNavbar';
import { FormErrors } from './FormErrors';
import './Error.css';
class FilmEdit extends Component {
    
    constructor(props) {
    super(props);
    this.state = {
        name: '',
        rating: 0,
        gid: -1,
        formErrors: {name: '', rating: '', gid: ''},
        nameValid: false,
        ratingValid: false,
        gidValid: false,
        formValid: false
    };
    this.handleChange = this.handleChange.bind(this);
    this.handleSubmit = this.handleSubmit.bind(this);
  }

  async componentDidMount() {
    let id = this.props.match.params.id;
    //id = id.substring(1);
    console.log(id);
    if (id !== 'new') {
      const film = await (await fetch('/v1/api/films/'+id).then(response => response.json()));
      this.setState({
          name: film.name,
          rating: film.rating,
          gid: film.genre.id,
          gname: film.genre.name,
          formErrors: {name: '', rating: '', gid: ''},
          nameValid: true,
          ratingValid: true,
          gidValid: true,
          formValid: true
      }, () => {for(let p in this.state){ this.validateField(p,this.state[p]);}});
    }
  }

  handleChange(event) {
    const target = event.target;
    const value = target.value;
    const name = target.name;
    this.setState({[name]: value},
                  () => { this.validateField(name, value) });
  }

  async handleSubmit(event) {
    event.preventDefault();
    let fid = 0;
    if (this.props.match.params.id !== 'new')
        fid = this.props.match.params.id;
    const item = {
        name:this.state.name,
        rating:this.state.rating,
        gid: this.state.gid
    }
    
    await fetch('/v1/api/films/'+fid, {
      method: 'PATCH',
      headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(item),
    });
    this.props.history.push('/v1/views/films');
  }

  validateField(fieldName, value) {
    const item = this.state;
    let fieldValidationErrors = item.formErrors;
    let nameValid = item.nameValid;
    let ratingValid = item.ratingValid;
    let gidValid = item.gidValid;

    switch(fieldName) {
      case 'name':
        nameValid = value.match(/^[A-Za-z\s]+$/);
        fieldValidationErrors.name = nameValid ? '' : ' is invalid. Require words!';
        break;
      case 'rating':
        ratingValid = value >= 0 && value <= 100;
        fieldValidationErrors.rating = ratingValid ? '': ' Must be in [0..100].';
        break;
      case 'gid':
        gidValid = value >= -1;
        fieldValidationErrors.gid = gidValid ? '' : 'Must be greater than or equal -1';
      default:
        break;
    }
    this.setState({formErrors: fieldValidationErrors,
                    nameValid: nameValid,
                    ratingValid: ratingValid,
                    gidValid: gidValid
                  }, () => this.validateForm());
  }

  validateForm() {
      this.setState({formValid: this.state.nameValid && this.state.ratingValid && this.state.gidValid});
      console.log(this.state.formValid);
      this.moveToP();
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
  
  errorClass(error) {
    return(error.length === 0 ? '' : ' has-error');
  }
  
  render() {
    const item = this.state;
    const title = <h2>{item.name !== '' ? 'Edit Film' : 'Add Film'}</h2>;

    return <div>
      <AppNavbar meid={2}/>
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
          <FormGroup className={'form-group'+this.errorClass(item.formErrors.rating)}>
            <Label htmlFor="rating">Rating</Label>
            <Input type="text" name="rating" id="rating" className="form-control" value={item.rating || ''}
                   onChange={this.handleChange} autoComplete="rating"/>
          </FormGroup>
          <FormGroup className={'form-group'+this.errorClass(item.formErrors.gid)}>
            <Label htmlFor="gid">Genre</Label>
            <Input type="text" name="gid" id="gid" className="form-control" value={item.gid || ''}
                   onChange={this.handleChange} autoComplete="gid"/>
          </FormGroup>
          <FormGroup>
            <Button color="primary" type="submit" className="btn btn-primary" disabled={!item.formValid}>Save</Button>{' '}
            <Button color="secondary" tag={Link} to="/views/films">Cancel</Button>
          </FormGroup>
        </Form>
      </Container>
    </div>
  }
    
}
export default withRouter(FilmEdit);