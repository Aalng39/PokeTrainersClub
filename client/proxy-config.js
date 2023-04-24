const proxy_config = [
    {
      context: [ '/**' ],
      target: 'http://localhost:8080',
      secure: false
    }
  ]
  
  module.exports = proxy_config