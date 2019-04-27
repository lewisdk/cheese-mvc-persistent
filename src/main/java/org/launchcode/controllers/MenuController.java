package org.launchcode.controllers;

import org.launchcode.models.Cheese;
import org.launchcode.models.Menu;
import org.launchcode.models.data.CheeseDao;
import org.launchcode.models.data.MenuDao;
import org.launchcode.models.forms.AddMenuItemForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Path;
import javax.validation.Valid;

@Controller
@RequestMapping(value = "menu")

public class MenuController {

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private CheeseDao cheeseDao;

    @RequestMapping(value = "")
    public String index(Model model) {
        model.addAttribute("title", "Menus");
        model.addAttribute("menuDao", menuDao.findAll());

        return "menu/index";

    }

    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String add(Model model){
        model.addAttribute("title", "Add Menu");
        model.addAttribute(new Menu());

        return "menu/add";
    }
    @RequestMapping(value = "add", method = RequestMethod.POST)
    public String add(Model model, @ModelAttribute @Valid Menu menu, Errors errors) {
        if (errors.hasErrors()) {
            model.addAttribute("title", "Add Menu");

            return "menu/add";
        }
        menuDao.save(menu);

        return "redirect:view/"+ menu.getId();
    }
    @RequestMapping(value = "view/{id}", method = RequestMethod.GET)
    public String viewMenu(Model model, @PathVariable int id){

        AddMenuItemForm addMenuItemForm = new AddMenuItemForm();
        Menu menu = menuDao.findOne(id);
        Iterable<Cheese> cheeses = addMenuItemForm.getCheeses();

        model.addAttribute("title", menu.getName());
        model.addAttribute("menu", menu);
        model.addAttribute("cheeses", cheeses);

        return"menu/view";
    }
    @RequestMapping(value="add-item/{id}", method=RequestMethod.GET)
    public String addItem(Model model, @PathVariable int id) {
        Iterable<Cheese> cheeses = cheeseDao.findAll();
        Menu menu = menuDao.findOne(id);
        AddMenuItemForm newMenuItemForm = new AddMenuItemForm(cheeseDao.findAll(), menu);

        model.addAttribute("form", newMenuItemForm);
        model.addAttribute("title", "Add item to menu:" + menu.getName());

        return "menu/add-item";
    }

    @RequestMapping(value="add-item/{id}",method = RequestMethod.POST)
    public String processAddItem(Model model, Errors errors, @ModelAttribute @Valid AddMenuItemForm addMenuItemForm, @PathVariable int id){

        if (errors.hasErrors()){
            model.addAttribute("form", addMenuItemForm);
            model.addAttribute("title", "Add item to menu: "+addMenuItemForm.getMenu().getName());
            return "menu/add-item";
        }
        Cheese cheeses = cheeseDao.findOne(addMenuItemForm.getCheeseId());
        Menu menu = menuDao.findOne(addMenuItemForm.getMenuId());

        menu.addItem(cheeses);
        menuDao.save(menu);
        return "redirect:/menu/view/"+menu.getId();




    }
}
