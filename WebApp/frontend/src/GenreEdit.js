import React, { Component } from 'react';
import { Link, withRouter } from 'react-router-dom';
import { Button, Container, Form, FormGroup, Input, Label } from 'reactstrap';
import AppNavbar from './AppNavbar';
import { FormErrors } from './FormErrors';
import './Error.css';
class GenreEdit extends Component {


  constructor(props) {
    super(props);
    this.state = {
        name: '',
        remarks: '',
        formErrors: {name: '', remarks: ''},
        nameValid: false,
        remarksValid: true,
        formValid: false
    };
    this.handleChange = this.handleChange.bind(this);
    this.handleSubmit = this.handleSubmit.bind(this);
  }

  async componentDidMount() {
    if (this.props.match.params.id !== 'new') {
      const genre = await (await fetch('$/v1/api/genres/{this.props.match.params.id}')).json();
      this.setState({
          name: genre.name,
          remarks: genre.remarks,
          formErrors: {name: '', remarks: ''},
          nameValid: true,
          remarksValid: true,
          formValid: true
      },() => {for(let p in this.state){ this.validateField(p,this.state[p]);} });
      
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
     for(let p in this.state){
            this.validateField(p,this.state[p]);
     }
     if(!this.state.formValid){
            alert('Validation failed!');
            return -1;
        }
    const item = {
        name:this.state.name,
        remarks:this.state.remarks === '' ? null : this.state.remarks
    }

    await fetch('/v1/api/genres/create', {
      method: (item.id) ? 'PUT' : 'POST',
      headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(item),
    });
    this.props.history.push('/views/genres');
  }

  validateField(fieldName, value) {
    const item = this.state;
    let fieldValidationErrors = item.formErrors;
    let nameValid = item.nameValid;
    let remarksValid = item.remarksValid;

    switch(fieldName) {
      case 'name':
        nameValid = value.match(/^[A-Z][a-z_]+$/);
        fieldValidationErrors.name = nameValid ? '' : ' is invalid. Require words!';
        nameValid = nameValid && value.length > 5;
        fieldValidationErrors.name = nameValid ? '' : fieldValidationErrors.name+' Length must be more than 5 characters!';
        break;
      case 'remarks':
        remarksValid = value.length === 0 || (value.length > 0 && value.match(/^$|^[A-Za-z,\s]+[.]$/)); //^(.*?\=\s*\w)(.*)$/
        fieldValidationErrors.remarks = remarksValid ? '': ' Must be empty or Sentence form.';
        break;
      default:
        break;
    }
    this.setState({formErrors: fieldValidationErrors,
                    nameValid: nameValid,
                    remarksValid: remarksValid
                  }, this.validateForm);
  }

  validateForm() {
      this.setState({formValid: this.state.nameValid && this.state.remarksValid});
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
    const title = <h2>{item.id ? 'Edit Genre' : 'Add Genre'}</h2>;

    return <div>
      <AppNavbar meid={1}/>
      <Container>
        {title}
        <Form onSubmit={this.handleSubmit}>
          <div className="panel panel-default">
            <FormErrors formErrors={item.formErrors} id='erp'/>
          </div>
          <FormGroup className={'form-group'+this.errorClass(item.formErrors.name)}>
            <Label htmlFor="name">Name</Label>
            <Input type="text" name="name" id="name" className="form-control" value={item.name || ''}
                   onChange={this.handleChange} autoComplete="name"/>
          </FormGroup>
          <FormGroup className={'form-group'+this.errorClass(item.formErrors.remarks)}>
            <Label htmlFor="remarks">Remarks</Label>
            <Input type="text" name="remarks" id="remarks" className="form-control" value={item.remarks || ''}
                   onChange={this.handleChange} autoComplete="remarks"/>
          </FormGroup>
          <FormGroup>
            <Button color="primary" type="submit" className="btn btn-primary" disabled={!item.formValid}>Save</Button>{' '}
            <Button color="secondary" tag={Link} to="/views/genres">Cancel</Button>
          </FormGroup>
        </Form>
      </Container>
    </div>
  }
}

export default withRouter(GenreEdit);