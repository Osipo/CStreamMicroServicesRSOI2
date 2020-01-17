import React, { Component } from 'react';
import { Collapse, Nav, Navbar, NavbarBrand, NavbarToggler, NavItem, NavLink } from 'reactstrap';
import { Link, withRouter } from 'react-router-dom';
class AppNavbar extends Component {
  constructor(props) {
    super(props);
    this.toggle = this.toggle.bind(this);
    this.state = {isOpen: false};
    console.log("builded. VID: ",this.props.meid);
    
  }

  toggle() {
    this.setState({
      isOpen: !this.state.isOpen
    });
  }

  componentWillMount(){
      this.unlisten = this.props.history.listen((loc,act) => {
          console.log("The current URL is ",loc.pathname,loc.search,loc.hash);
          console.log("The last navigation action was ",act);
      });
  }
  componentWillUnmount(){
      this.unlisten();
  }
  
  componentDidMount(){
      let a = [];
      a = document.getElementsByClassName('menu-app');
      console.log(a.length);
      Array.prototype.forEach.call(a, function(el, index, arr){el.className = this.props.meid === index ? el.className + " active-l" : el.className},this);
  }
  
  render() {
    return <Navbar color="dark" dark expand="md">
      <NavbarBrand to="/" tag={Link} className="menu-app">Home</NavbarBrand>
      <NavbarBrand to="/views/genres" tag={Link} className="menu-app">Genres</NavbarBrand>
      <NavbarBrand tag={Link} to="/views/films" className="menu-app">Films</NavbarBrand>
      <NavbarBrand tag={Link} to="/views/cinemas" className="menu-app">Cinemas</NavbarBrand>
      <NavbarBrand tag={Link} to="/views/seances" className="menu-app">Seances</NavbarBrand>
      <NavbarBrand tag={Link} to="/views/login" id="last-nav-m" className="menu-app">Login</NavbarBrand>
      <NavbarToggler onClick={this.toggle}/>
      <Collapse isOpen={this.state.isOpen} navbar>
        <Nav className="ml-auto" navbar>
          <NavItem>
            <NavLink href="https://github.com/Osipo/CStreamMicroServicesRSOI2">GitHub</NavLink>
          </NavItem>
        </Nav>
      </Collapse>
    </Navbar>;
  }
}
export default withRouter(AppNavbar);