require 'test_helper'

class FoursquareControllerTest < ActionDispatch::IntegrationTest
  test "should get index" do
    get foursquare_index_url
    assert_response :success
  end

  test "should get show" do
    get foursquare_show_url
    assert_response :success
  end

  test "should get create" do
    get foursquare_create_url
    assert_response :success
  end

  test "should get update" do
    get foursquare_update_url
    assert_response :success
  end

end
