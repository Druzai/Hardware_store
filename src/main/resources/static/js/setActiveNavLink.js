let pathname = window.location.pathname;

$('.nav-link').each(function () {
    if ((this.pathname === '/' && pathname === '/') || (this.pathname !== '/' && pathname.includes(this.pathname))) $(this).addClass('active');
    else if (this.className.includes('active')) $(this).removeClass('active');
});