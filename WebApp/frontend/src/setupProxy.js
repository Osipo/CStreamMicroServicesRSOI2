const { createProxyMiddleware } = require('http-proxy-middleware');
module.exports = function(app) {
  app.use(
    '/FilmService',
    createProxyMiddleware({
      target: 'http://localhost:3333',
      changeOrigin: true,
    })
  );

  app.use(
    '/CinemaService',
    createProxyMiddleware({
      target: 'http://localhost:2222',
      changeOrigin: true,
    })
  );
  app.use(
    '/SeanceService',
    createProxyMiddleware({
      target: 'http://localhost:1111',
      changeOrigin: true,
    })
  );
  app.use(
    '/OrderService',
    createProxyMiddleware({
      target: 'http://localhost:4444',
      changeOrigin: true,
    })
  );
};