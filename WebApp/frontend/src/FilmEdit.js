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
        gname: '',
        formErrors: {name: '', rating: '', gname: ''},
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
          gname: film.genre.name,
          formErrors: {name: '', rating: '', gname: ''},
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
    
    for(let p in this.state){
        this.validateField(p,this.state[p]);
    }
        if(!this.state.formValid){
        alert('Validation failed!');
        return -1;
    }
    const genre  = await (await fetch('/v1/api/genres/name/'+this.state.gname).then(response => response.json()));
    let s = {
        name:this.state.name,
        rating:this.state.rating,
        gname: this.state.gname
    }
    console.log(genre);
    if(genre.length === 1){
        console.log("Array found");
        s = {name:this.state.name,
        rating:Number(this.state.rating === "" ? 0 : this.state.rating),
        gid: genre[0].id};
        await fetch('/v1/api/films/'+fid, {
          method: 'PATCH',
          headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
          },
          body: JSON.stringify(s),
        });
        this.props.history.push('/views/films');
    }
    else{
        alert(genre.reason);
        return -1;
    }
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
      case 'gname':
        gidValid = value.match(/^[A-Za-z\s]+$/);
        fieldValidationErrors.gname = gidValid ? '' : 'is invalid. Require words!';
        gidValid = gidValid && value.length > 3;
        fieldValidationErrors.gname = gidValid ? '' : fieldValidationErrors.gname + ' Length must be greater than 3 characters!';
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
          <FormGroup className={'form-group'+this.errorClass(item.formErrors.gname)}>
            <Label htmlFor="gname">Genre</Label>
            <Input type="text" name="gname" id="gname" className="form-control" value={item.gname || ''}
                   onChange={this.handleChange} autoComplete="gname"/>
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